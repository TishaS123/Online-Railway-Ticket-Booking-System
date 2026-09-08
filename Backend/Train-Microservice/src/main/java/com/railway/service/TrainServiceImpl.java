package com.railway.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.entity.Train;
import com.railway.repository.TrainRepository;

@Service
public class TrainServiceImpl implements TrainService {

	@Autowired
	private TrainRepository trainRepository;
	
	
	@Override
	public void addTrainDetails(Train train) {
		trainRepository.save(train);
	}

	@Override
	public Train getTrainById(int id) {
		return trainRepository.findByTrainId(id);
	}

	@Override
	public List<Train> getAllTrain() {
		return trainRepository.findAll();
	}

	@Override
	public List<Train> getTrainBySourceAndDestination(String source, String destination) {
		return trainRepository.findTrainBySourceAndDestination(source,destination);
	}

	@Override
	public Train updateTrain(int id, Train train) {
		Train existing = getTrainById(id);
		existing.setTrainName(train.getTrainName());
		existing.setArrivalTime(train.getArrivalTime());
		existing.setDepartureTime(train.getDepartureTime());
		existing.setDestination(train.getDestination());
		existing.setSource(train.getSource());
		existing.setDate(train.getDate());
		return trainRepository.save(existing);
	}

	@Override
	public void delete(int id) {
		trainRepository.deleteById(id);
	}

	@Override
	public Train getTrainByIdAndSourceAndDestinationAndDate(int trainId, String source, String destination,
			LocalDate date) {
		return trainRepository.findByTrainIdAndSourceAndDestinationAndDate(trainId,source,destination,date);
	}

}
