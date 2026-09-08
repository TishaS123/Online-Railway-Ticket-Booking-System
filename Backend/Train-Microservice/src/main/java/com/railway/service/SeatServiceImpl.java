package com.railway.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.railway.entity.Coaches;
import com.railway.entity.Seat;
import com.railway.entity.SeatStatus;
import com.railway.exception.CoachNotFoundException;
import com.railway.exception.SeatBookingConflictException;
import com.railway.repository.SeatRepository;

@Service
public class SeatServiceImpl implements SeatService {
	
	private static final Duration HOLD_TTL = Duration.ofMinutes(3);
	
	@Autowired
	private SeatRepository seatRepo;
	
	@Autowired
	private CoachService service;

	@Override
	public void addSeat(Seat seat, int coachId) throws CoachNotFoundException {
		Coaches coach = service.getCoaches(coachId);
		seat.setCoach(coach);
		seatRepo.save(seat);
	}

	@Override
	public Seat getSeatByCoachId(int coachId) {
		return seatRepo.findByCoach_CoachId(coachId);
	}

	@Override
	public List<Seat> getAllSeats() {
		return seatRepo.findAll();
	}

	@Override
	public Seat getSeatById(Long id) {
		return seatRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Seat not found with id: " + id));
	}

	@Override
	public List<Seat> getAvailableSeatsByCoachId(int coachId) {
		return seatRepo.findByCoach_CoachIdAndStatus(coachId, SeatStatus.AVAILABLE);
	}

	@Override
	@Transactional
	public void holdSeats(List<Long> seatId) {
		validateSeatInput(seatId);
		Set<Long> requestedSeatIds = new HashSet<>(seatId);
		List<Seat> seats = seatRepo.findAllByIdForUpdate(seatId);
		if (seats.size() != requestedSeatIds.size()) {
			throw new SeatBookingConflictException("One or more selected seats are invalid.");
		}

		LocalDateTime now = LocalDateTime.now();
		for (Seat seat : seats) {
			if (seat.getStatus() == SeatStatus.BOOKED) {
				throw new SeatBookingConflictException("Seat " + seat.getSeatNumber() + " is already booked by another user. Please choose another seat.");
			}
			if (seat.getStatus() == SeatStatus.LOCKED) {
				if (seat.getHoldExpiresAt() != null && seat.getHoldExpiresAt().isAfter(now)) {
					throw new SeatBookingConflictException("Seat " + seat.getSeatNumber() + " is temporarily locked for another booking. Please try again later.");
				}
				seat.setStatus(SeatStatus.AVAILABLE);
				seat.setHeldByUserId(null);
				seat.setHoldExpiresAt(null);
			}
			seat.setStatus(SeatStatus.LOCKED);
			seat.setHeldByUserId(null);
			seat.setHoldExpiresAt(now.plus(HOLD_TTL));
		}

		try {
			seatRepo.saveAllAndFlush(seats);
		} catch (ObjectOptimisticLockingFailureException | OptimisticLockException ex) {
			throw new SeatBookingConflictException("Seat booking conflict detected. Another user is updating the same seat. Please retry.");
		}
	}

	@Override
	@Transactional
	public void markSeatsAsBooked(List<Long> seatId) {
		validateSeatInput(seatId);
		Set<Long> requestedSeatIds = new HashSet<>(seatId);
		List<Seat> seats = seatRepo.findAllByIdForUpdate(seatId);
		if (seats.size() != requestedSeatIds.size()) {
			throw new SeatBookingConflictException("One or more selected seats are invalid.");
		}
		for (Seat seat : seats) {
			if (seat.getStatus() == SeatStatus.BOOKED) {
				throw new SeatBookingConflictException(
						"Seat " + seat.getSeatNumber() + " is already booked by another user. Please choose another seat.");
			}
		}
		confirmSeatsBooked(seatId);
	}

	@Override
	@Transactional
	public void confirmSeatsBooked(List<Long> seatId) {
		validateSeatInput(seatId);
		Set<Long> requestedSeatIds = new HashSet<>(seatId);
		List<Seat> seats = seatRepo.findAllByIdForUpdate(seatId);
		if (seats.size() != requestedSeatIds.size()) {
			throw new SeatBookingConflictException("One or more selected seats are invalid.");
		}

		LocalDateTime now = LocalDateTime.now();
		for (Seat seat : seats) {
			if (seat.getStatus() != SeatStatus.LOCKED) {
				throw new SeatBookingConflictException("Seat " + seat.getSeatNumber() + " is not in a valid locked state for booking.");
			}
			if (seat.getHoldExpiresAt() == null || seat.getHoldExpiresAt().isBefore(now)) {
				seat.setStatus(SeatStatus.AVAILABLE);
				seat.setHeldByUserId(null);
				seat.setHoldExpiresAt(null);
				throw new SeatBookingConflictException("Seat " + seat.getSeatNumber() + " hold has expired. Please try again.");
			}
			seat.setStatus(SeatStatus.BOOKED);
			seat.setHoldExpiresAt(null);
			seat.setHeldByUserId(null);
		}

		try {
			seatRepo.saveAllAndFlush(seats);
		} catch (ObjectOptimisticLockingFailureException | OptimisticLockException ex) {
			throw new SeatBookingConflictException("Seat booking conflict detected. Another user is updating the same seat. Please retry.");
		}
	}

	@Override
	public void markSeatsAsAvailable(List<Long> seatId) {
		List<Seat> seats = seatRepo.findAllById(seatId);
		for(Seat seat : seats) {
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setHoldExpiresAt(null);
			seat.setHeldByUserId(null);
		}
		seatRepo.saveAll(seats);
	}

	@Override
	@Transactional
	public void releaseExpiredHolds() {
		List<Seat> expiredSeats = seatRepo.findExpiredHolds(SeatStatus.LOCKED, LocalDateTime.now());
		for (Seat seat : expiredSeats) {
			seat.setStatus(SeatStatus.AVAILABLE);
			seat.setHoldExpiresAt(null);
			seat.setHeldByUserId(null);
		}
		if (!expiredSeats.isEmpty()) {
			seatRepo.saveAll(expiredSeats);
		}
	}

	@Scheduled(fixedDelay = 30000)
	public void scheduledReleaseExpiredHolds() {
		releaseExpiredHolds();
	}

	private void validateSeatInput(List<Long> seatId) {
		if (seatId == null || seatId.isEmpty()) {
			throw new SeatBookingConflictException("No seats selected for booking.");
		}
	}
	
}
