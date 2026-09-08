package com.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.client.RestClientException;

import com.reservation.dto.TicketRequest;
import com.reservation.entity.Passenger;
import com.reservation.entity.Ticket;
import com.reservation.entity.TicketStatus;
import com.reservation.exception.ReservationNotFoundException;
import com.reservation.exception.SeatNotAvailableException;
import com.reservation.exception.TicketAlreadyCancelled;

public interface ReservationService {
	
	Ticket reserveTicket(TicketRequest request) throws SeatNotAvailableException, ReservationNotFoundException;
	Ticket makePayment(Long ticketNo) throws ReservationNotFoundException, SeatNotAvailableException;
	String generatePNR();
	Ticket cancelTicket(String pnrNo) throws ReservationNotFoundException, TicketAlreadyCancelled;
	void updateTicketStatus(String pnrNo, TicketStatus status) throws ReservationNotFoundException;
	Ticket getTicketById(Long ticketNo);
	List<Ticket> getAllTickets();
}
