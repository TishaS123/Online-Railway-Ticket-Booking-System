package com.railway.exception;

public class SeatBookingConflictException extends RuntimeException {

    public SeatBookingConflictException() {
        super("Seat booking conflict: another user is already booking this seat.");
    }

    public SeatBookingConflictException(String message) {
        super(message);
    }
}
