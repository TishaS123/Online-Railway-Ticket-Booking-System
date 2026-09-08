package com.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(SeatNotAvailableException.class)
	public ResponseEntity<String> handleSeatNotAvailableException(SeatNotAvailableException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ReservationNotFoundException.class)
	public ResponseEntity<String> handleReservationNotFoundException(ReservationNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TicketAlreadyCancelled.class)
	public ResponseEntity<String> handleTicketAlready(TicketAlreadyCancelled ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
