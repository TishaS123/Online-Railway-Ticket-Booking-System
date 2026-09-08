package com.reservation.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.DeleteExchange;

import com.reservation.dto.TicketRequest;
import com.reservation.entity.Ticket;
import com.reservation.entity.TicketStatus;
import com.reservation.exception.ReservationNotFoundException;
import com.reservation.exception.SeatNotAvailableException;
import com.reservation.exception.TicketAlreadyCancelled;
import com.reservation.service.ReservationService;

@RestController
@RequestMapping("/booking")
public class ReservationController {
	
	@Autowired
	private ReservationService service;
	
	@PreAuthorize("hasRole('PASSENGER')")
	@PostMapping("/book")
	public ResponseEntity<Ticket> bookTicket(@RequestBody TicketRequest request) throws SeatNotAvailableException, ReservationNotFoundException{
		return new ResponseEntity<>(service.reserveTicket(request), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('PASSENGER')")
	@PostMapping("/make-payment")
	public ResponseEntity<Ticket> makePayment(@RequestBody Ticket ticket) throws ReservationNotFoundException, SeatNotAvailableException {
		if (ticket == null || ticket.getTicketNo() == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(service.makePayment(ticket.getTicketNo()), HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAnyRole('ADMIN','PASSENGER')")
	@GetMapping("/ticket/{id}")
	public ResponseEntity<Ticket> getTicket(@PathVariable Long id){
		return ResponseEntity.ok(service.getTicketById(id));
	}
	
	
	@PreAuthorize("hasRole('PASSENGER')")
	@PutMapping("/update-status/{pnrNo}")
	public ResponseEntity<String> updateTicketStatus(@PathVariable String pnrNo, @RequestParam TicketStatus status) throws ReservationNotFoundException{
		service.updateTicketStatus(pnrNo, status);
		return ResponseEntity.ok("Ticket status updated to: " + status);
	}
	
	@PreAuthorize("hasRole('PASSENGER')")
	@PutMapping("/cancelTicket")
	public ResponseEntity<String> cancelTicket(@RequestParam String pnrNo) throws ReservationNotFoundException, TicketAlreadyCancelled{
		service.cancelTicket(pnrNo);
		return ResponseEntity.ok("Ticket cancelled successfully..");
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/showAllReservations")
	public ResponseEntity<List<Ticket>> showAllReservations(){
		return new ResponseEntity<>(service.getAllTickets(), HttpStatus.OK);
	}
}
