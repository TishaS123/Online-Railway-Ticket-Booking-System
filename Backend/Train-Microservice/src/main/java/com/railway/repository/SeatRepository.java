package com.railway.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.railway.entity.Coaches;
import com.railway.entity.Seat;
import com.railway.entity.SeatStatus;

public interface SeatRepository extends JpaRepository<Seat, Long>{

	Seat findByCoach_CoachId(int coachId);

	List<Seat> findByCoach_CoachIdAndStatus(int coachId, SeatStatus status);

	List<Seat> findByStatus(SeatStatus status);

	Optional<Seat> findBySeatNumberAndCoach_CoachId(String seatNumber, int coachId);

	@Lock(LockModeType.OPTIMISTIC)
	@Query("select s from Seat s where s.id in :seatIds")
	List<Seat> findAllByIdForUpdate(@Param("seatIds") List<Long> seatIds);

	@Query("select s from Seat s where s.status = :status and s.holdExpiresAt is not null and s.holdExpiresAt < :now")
	List<Seat> findExpiredHolds(@Param("status") SeatStatus status, @Param("now") LocalDateTime now);

}
