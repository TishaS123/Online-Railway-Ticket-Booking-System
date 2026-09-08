package com.railway.service;

import java.time.LocalDate;
import java.util.List;

import com.railway.entity.Train;

public interface TrainService {
	void addTrainDetails(Train train);
	Train getTrainById(int id);
	List<Train> getAllTrain();
	Train updateTrain(int id, Train train);
	void delete(int id);
	Train getTrainByIdAndSourceAndDestinationAndDate(int trainId, String source, String destination, LocalDate date);
	List<Train> getTrainBySourceAndDestination(String source, String destination);
}
