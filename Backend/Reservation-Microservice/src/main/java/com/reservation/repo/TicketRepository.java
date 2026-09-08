package com.reservation.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.reservation.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long>{
	
	int countByTrainIdAndClassTypeAndStatus(int trainId, String classType, String status);

	@Query("SELECT COALESCE(SUM(t.totalPassengers),0) FROM Ticket t WHERE t.trainId = : trainId AND t.classType = : classType")
	int sumtotalPassengersByTrainTrainIdAndClassType(int trainId, String classType);

	Optional<Ticket> findByPnrNo(String pnrNo);

	Optional<Ticket> findByIdempotencyKey(String idempotencyKey);

}
