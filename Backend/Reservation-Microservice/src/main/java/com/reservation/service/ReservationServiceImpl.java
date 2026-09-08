package com.reservation.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.FeignException;

import com.reservation.dto.Coach;
import com.reservation.dto.PaymentRequest;
import com.reservation.dto.PaymentResponse;
import com.reservation.dto.SeatIdentifier;
import com.reservation.dto.TicketRequest;
import com.reservation.dto.Train;
import com.reservation.entity.Passenger;
import com.reservation.entity.Seat;
import com.reservation.entity.Ticket;
import com.reservation.entity.TicketStatus;
import com.reservation.exception.ReservationNotFoundException;
import com.reservation.exception.SeatNotAvailableException;
import com.reservation.exception.TicketAlreadyCancelled;
import com.reservation.feign.PaymentServiceClient;
import com.reservation.feign.TrainServiceClient;
import com.reservation.repo.PassengerRepository;
import com.reservation.repo.TicketRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private PassengerRepository passengerRepository;
	
	@Autowired
	private TrainServiceClient trainClient;
	
//	@Autowired
//	private PaymentServiceClient paymentClient;
	
	@Autowired
	private ResilientPaymentService resilientPaymentService;
		
	@Autowired
	private EmailService emailService;
	

	@Override
	public String generatePNR() {
		return UUID.randomUUID().toString().substring(0,8).toUpperCase();
	}


	@Override
	public Ticket reserveTicket(TicketRequest request) throws SeatNotAvailableException, ReservationNotFoundException {
		String authorization = getAuthorizationHeader();
		if (request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()) {
			Optional<Ticket> existingTicket = ticketRepository.findByIdempotencyKey(request.getIdempotencyKey());
			if (existingTicket.isPresent()) {
				return existingTicket.get();
			}
		}
		
//		Step 1 : CREATE TICKET WITH PENDING STATUS
		
		/**
		 * Used Rest Template For Inter-Service Communication
		 */
//		Train trainDetails = rest.getForObject("http://localhost:8002/train/getTrainByIdSourceDestinationDate?trainId="+request.getTrainId()+"&source="+request.getSource()+"&destination="+request.getDestination()+"&date="+request.getDate(), Train.class);
		
		
		/**
		 * Used Feign Client For Inter-Service Communication
		 */
		LocalDate bookingDate = request.getDate();
		String isoDate = bookingDate != null ? bookingDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		Train trainDetails = trainClient.getTrainByIdSourceDestinationDate(authorization, request.getTrainId(), request.getSource(), request.getDestination(), isoDate);
		int coachId = -1;
		
		
//		double fare = rest.getForObject("http://localhost:8002/fare/getFareByRouteAndClassType?source="+request.getSource()+"&destination="+request.getDestination()+"&classType="+request.getClassType(), Double.class);
		
		double fare = trainClient.getFareByRouteAndType(authorization, request.getSource(), request.getDestination(), request.getClassType());
		
		
		List<Coach> coaches = new ArrayList<>(trainDetails.getCoaches());
		
		Ticket ticket = new Ticket();
		
		int requiredSeats = request.getPassengers().size();
		
		boolean seatFound = false;
		
		double price = fare * requiredSeats;
		
		for(Coach c : coaches) {
			if(c.getClassType().equals(request.getClassType())) {
				seatFound = true;
				coachId = c.getCoachId();
				break;
			}
		}
		
		if(!seatFound) throw new SeatNotAvailableException("Seats Not Available");
		
//		List<Seat> availableSeats = trainClient.getAvailableSeatsByCoachId(coachId);
//		
//		
//		
//		if(availableSeats.size() < requiredSeats) {
//			throw new SeatNotAvailableException("Not enough available seats in coach");
//		}
//		
		List<Passenger> passengers = request.getPassengers();
//		List<Seat> assignedSeats = availableSeats.subList(0, requiredSeats);
//		for(int i=0;i<passengers.size();i++) {
//			passengers.get(i).setSeatNumber(assignedSeats.get(i).getSeatNumber());
//		}
		
		

		List<Seat> assignedSeats = new ArrayList<>();
		Map<String, String> seatToCoachMap = new HashMap<>();
		
// Step 2: Find available seats across coaches of the same class type
		for (Coach coach : trainDetails.getCoaches()) {
			if (!coach.getClassType().equals(request.getClassType())) continue;
		
			List<Seat> availableSeats = trainClient.getAvailableSeatsByCoachId(authorization, coach.getCoachId());
		

			for (Seat seat : availableSeats) {
				assignedSeats.add(seat);
				seatToCoachMap.put(seat.getSeatNumber(), coach.getCoachNumber());
				if (assignedSeats.size() == requiredSeats) break;
			}
			
			if (assignedSeats.size() == requiredSeats) break;
		}
		
		

		if (assignedSeats.size() < requiredSeats) {
			throw new SeatNotAvailableException("Not enough available seats in any coach of class type: " + request.getClassType());
		}
		
	// Step 3: Assign seats and coach numbers to passengers
		for (int i = 0; i < passengers.size(); i++) {
			Passenger passenger = passengers.get(i);
			Seat seat = assignedSeats.get(i);
			passenger.setSeatNumber(seat.getSeatNumber());
			passenger.setCoachNumber(seatToCoachMap.get(seat.getSeatNumber()));
		}

		
		
		try {
			trainClient.holdSeats(authorization, assignedSeats.stream().map(Seat::getId).collect(Collectors.toList()));
		} catch (FeignException.Conflict e) {
			throw new SeatNotAvailableException("Seat is temporarily locked by another user. Please choose another seat and retry.");
		} catch (FeignException.Forbidden e) {
			throw new SeatNotAvailableException("Seat locking was rejected by Train service. Please retry after backend restart.");
		}
		
			
		ticket.setTrainId(request.getTrainId());
		ticket.setClassType(request.getClassType());
		ticket.setArrivalTime(trainDetails.getArrivalTime());
		ticket.setDate(request.getDate());
		ticket.setDepartureTime(trainDetails.getDepartureTime());
		ticket.setPnrNo(generatePNR());
		String idempotencyKey = request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()
				? request.getIdempotencyKey()
				: UUID.randomUUID().toString();
		ticket.setIdempotencyKey(idempotencyKey);
		ticket.setSource(request.getSource());
		ticket.setDestination(request.getDestination());
		ticket.setStatus(TicketStatus.PENDING);
	    ticket.setTrainName(trainDetails.getTrainName());
		ticket.setTotalAmount(price);
		ticket.setCoachId(coachId);
		
		ticket.setPassengers(passengers);
		ticket.setTotalPassengers(requiredSeats);
		ticketRepository.save(ticket);
		for(Passenger passenger : passengers) {
			passenger.setTicket(ticket);
		}
		passengerRepository.saveAll(passengers);
		
		return ticket;
	}

	@Override
	public Ticket makePayment(Long ticketNo) throws ReservationNotFoundException, SeatNotAvailableException {
		String authorization = getAuthorizationHeader();
		Ticket ticket = ticketRepository.findById(ticketNo)
				.orElseThrow(() -> new ReservationNotFoundException("Ticket not found with id: " + ticketNo));

		if (ticket.getStatus() != TicketStatus.PENDING) {
			return ticket;
		}

		List<SeatIdentifier> seatIdentifiers = buildSeatIdentifiers(ticket);
		List<Long> seatIds = trainClient.getSeatIds(authorization, seatIdentifiers);

		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setAmount((int) (ticket.getTotalAmount() * 100));
		paymentRequest.setCurrency("inr");
		paymentRequest.setDescription("TRAIN TICKET BOOKING");
		paymentRequest.setEmail(ticket.getPassengers().isEmpty() ? null : ticket.getPassengers().get(0).getEmail());
		paymentRequest.setPnrNo(ticket.getPnrNo());
		paymentRequest.setIdempotencyKey(ticket.getIdempotencyKey());

//		ResponseEntity<PaymentResponse> response;
//		try {
//			response = paymentClient.createCharge(paymentRequest);
//		} catch (Exception paymentException) {
//			trainClient.markSeatAsAvailable(authorization, seatIds);
//			ticket.setStatus(TicketStatus.CANCELLED);
//			ticketRepository.save(ticket);
//			throw new SeatNotAvailableException("Payment service unavailable. Seat lock released.");
//		}
		
		ResponseEntity<PaymentResponse> response =
		        resilientPaymentService.createCharge(paymentRequest);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null
				&& "succeeded".equalsIgnoreCase(response.getBody().getStatus())) {
			try {
				trainClient.confirmSeatsBooked(authorization, seatIds);
				ticket.setStatus(TicketStatus.CONFIRMED);
			} catch (Exception confirmationException) {
				ticket.setStatus(TicketStatus.PENDING);
				ticketRepository.save(ticket);
				throw new SeatNotAvailableException("Payment succeeded but seat confirmation is pending. Please contact support.");
			}
		} else {
			trainClient.markSeatAsAvailable(authorization, seatIds);
			ticket.setStatus(TicketStatus.CANCELLED);
		}

		ticketRepository.save(ticket);
		sendTicketStatusEmails(ticket);
		return ticket;
	}


	
	
	@Override
	public Ticket cancelTicket(String pnrNo) throws ReservationNotFoundException, TicketAlreadyCancelled {
		String authorization = getAuthorizationHeader();
	    // Step 1: Fetch the ticket
	    Ticket ticket = ticketRepository.findByPnrNo(pnrNo)
	        .orElseThrow(() -> new ReservationNotFoundException("Ticket not found with PNR: " + pnrNo));

	    // Step 2: Check if already cancelled
	    if (ticket.getStatus() == TicketStatus.CANCELLED) {
	       throw new TicketAlreadyCancelled("Ticket already cancelled");
	    }

	    // Step 3: Prepare seat identifiers with trainId
	    List<Passenger> passengers = ticket.getPassengers();
	    List<SeatIdentifier> seatIdentifiers = buildSeatIdentifiers(ticket);

	    // Step 4: Get seat IDs from Train Service
	    List<Long> seatIds = trainClient.getSeatIds(authorization, seatIdentifiers);

	    // Step 5: Mark seats as available
	    
	    trainClient.markSeatAsAvailable(authorization, seatIds);

	    // Step 6: Update ticket status
	    ticket.setStatus(TicketStatus.CANCELLED);
	    ticketRepository.save(ticket);

	    // Step 7: Send cancellation emails
	    for (Passenger passenger : passengers) {
	        String to = passenger.getEmail();
	        String subject = "TICKET CANCELLED";
	        String contentText = "Dear " + passenger.getPassengerName() + ",\n\nYour ticket with PNR " + ticket.getPnrNo() + " has been cancelled.\n\nThank you.";
	        emailService.sendEmail(to, subject, contentText);
	    }

	    return ticket;
	}


	@Override
	public void updateTicketStatus(String pnrNo, TicketStatus status) throws ReservationNotFoundException {
		Ticket ticket = ticketRepository.findByPnrNo(pnrNo).orElseThrow(() -> new ReservationNotFoundException("Reservation Not Found with the given PNR number:"+pnrNo));
		ticket.setStatus(status);
		ticketRepository.save(ticket);
	}


	@Override
	public Ticket getTicketById(Long ticketNo) {
		return ticketRepository.findById(ticketNo).orElseThrow(() -> new RuntimeException("Ticket not found"));
	}


	@Override
	public List<Ticket> getAllTickets() {
		return ticketRepository.findAll();
	}

	private List<SeatIdentifier> buildSeatIdentifiers(Ticket ticket) {
		List<SeatIdentifier> seatIdentifiers = new ArrayList<>();
		for (Passenger passenger : ticket.getPassengers()) {
			SeatIdentifier identifier = new SeatIdentifier();
			identifier.setSeatNumber(passenger.getSeatNumber());
			identifier.setCoachNumber(passenger.getCoachNumber());
			identifier.setTrainId(ticket.getTrainId());
			seatIdentifiers.add(identifier);
		}
		return seatIdentifiers;
	}

	private void sendTicketStatusEmails(Ticket ticket) {
		for(Passenger passenger : ticket.getPassengers()) {
			String to = passenger.getEmail();
			String subject = "TICKET " + ticket.getStatus();
			String contentText = "Dear " + passenger.getPassengerName() + ",\n\nYour ticket with PNR " + ticket.getPnrNo()
					+ " is currently " + ticket.getStatus() + ".\nTrain: " + ticket.getTrainName() + "\nSource: " + ticket.getSource()
					+ "\nDestination: " + ticket.getDestination() + "\nJourney Date: " + ticket.getDate()
					+ "\nArrival Time: " + ticket.getArrivalTime() + "\nDeparture Time: " + ticket.getDepartureTime()
					+ "\n\nThank you for Booking with us.";
			emailService.sendEmail(to, subject, contentText);
		}
	}

	private String getAuthorizationHeader() {
		if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
			HttpServletRequest request = attributes.getRequest();
			String authorization = request.getHeader("Authorization");
			if (authorization != null && !authorization.isBlank()) {
				return authorization;
			}
		}
		return "";
	}
	

}
