package com.railway.service;

import java.util.List;

import com.railway.entity.Seat;
import com.railway.exception.CoachNotFoundException;

public interface SeatService {
	
	void addSeat(Seat seat, int coachId) throws CoachNotFoundException;
	Seat getSeatByCoachId(int coachId);
	List<Seat> getAllSeats();
	Seat getSeatById(Long id);
	List<Seat> getAvailableSeatsByCoachId(int coachId);
	void holdSeats(List<Long> seatId);
	void markSeatsAsBooked(List<Long> seatId);
	void confirmSeatsBooked(List<Long> seatId);
	void markSeatsAsAvailable(List<Long> seatId);
	void releaseExpiredHolds();
}
