package com.reservation.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.stereotype.Service;

import com.reservation.dto.TicketRequest;
import com.reservation.dto.TrainDetails;
import com.reservation.entity.Passenger;
import com.reservation.entity.Ticket;
import com.reservation.exception.SeatNotAvailableException;
import com.reservation.feign.TrainServiceClient;
import com.reservation.repo.PassengerRepository;
import com.reservation.repo.TicketRepository;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private PassengerRepository passengerRepository;
	
	@Autowired
	private TrainServiceClient trainClient;
	

	@Override
	public Ticket reserveTicket(TicketRequest request) throws SeatNotAvailableException {
	
		int availableSeats = trainClient.getAvailableSeats(request.getTrainId(), request.getClassType());
		
		if(availableSeats < request.getPassengers().size()) {
			throw new SeatNotAvailableException("Seats are not available...");
		}
		
		double farePerPassenger = trainClient.getFareByRouteAndType(request.getSource(), request.getDestination(), request.getClassType());
		double totalAmount = farePerPassenger * request.getPassengers().size();
		
		Ticket ticket = new Ticket();
		
		TrainDetails trainDetails = trainClient.getTrainByTrainId(request.getTrainId());
		
		
		ticket.setTrainId(trainDetails.getTrainId());
		ticket.setArrivalTime(trainDetails.getArrivalTime());
		ticket.setClassType(request.getClassType());
		ticket.setDepartureTime(trainDetails.getDepartureTime());
		ticket.setPnrNo(generatePNR());
		ticket.setTotalAmount(totalAmount);
		ticket.setSource(request.getSource());
		ticket.setDestination(request.getDestination());
		ticket.setDate(trainDetails.getDate());
		ticket.setTrainName(trainDetails.getTrainName());
		ticket.setBankName(request.getBankName());
		ticket.setTotalPassengers(request.getPassengers().size());
		ticket.setStatus("PENDING");
		
		
		List<Passenger> passengers = request.getPassengers().stream().map(p -> {
			Passenger passenger = new Passenger();
			passenger.setAge(p.getAge());
			passenger.setGender(p.getGender());
			passenger.setPassengerName(p.getName());
			passenger.setTicket(ticket);
			return passenger;
		}).collect(Collectors.toList());
		
		ticket.setPassengers(passengers);
		ticketRepository.save(ticket);
		passengerRepository.saveAll(passengers);
		
		
		return ticket;
	}

	@Override
	public String cancelTicket(String pnr) {
		return null;
	}

	@Override
	public int getAvailableSeats(int trainId, String classType) {
		return trainClient.getAvailableSeats(trainId, classType);
	}

	@Override
	public double getFare(String source, String destination, String classType) {
		return trainClient.getFareByRouteAndType(source, destination, classType);
	}

	@Override
	public String generatePNR() {
		return UUID.randomUUID().toString().substring(0,8).toUpperCase();
	}

	@Override
	public int getBookedSeats(int trainId, String classType) {
		return ticketRepository.sumtotalPassengersByTrainIdAndClassType(trainId, classType);
	}

}
