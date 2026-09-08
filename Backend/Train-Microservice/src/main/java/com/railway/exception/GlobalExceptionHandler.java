package com.railway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice 
public class GlobalExceptionHandler {

	@ExceptionHandler(CoachNotFoundException.class)
	public ResponseEntity<String> handleCoachNotFoundException(CoachNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException e){
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(FareNotFoundException.class)
	public ResponseEntity<String> handleFareNotFoundException(FareNotFoundException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(SeatBookingConflictException.class)
	public ResponseEntity<String> handleSeatBookingConflictException(SeatBookingConflictException ex){
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
	}
}
