package com.railway.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.railway.entity.Train;

public interface TrainRepository extends JpaRepository<Train, Integer> {

	Train findByTrainId(int trainId);

	List<Train> findTrainBySourceAndDestination(String source, String destination);

	Train findByTrainIdAndSourceAndDestinationAndDate(int trainId, String source, String destination, LocalDate date);

}
