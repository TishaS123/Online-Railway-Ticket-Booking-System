package com.railway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.railway.entity.Fare;
import com.railway.entity.Train;
import com.railway.exception.FareNotFoundException;
import com.railway.repository.FareRepository;

@Service
public class FareServiceImpl implements FareService{
	
	@Autowired
	private FareRepository fareRepository;
	
	@Autowired
	private TrainService service;
	

	@Override
	public Fare addFareDetails(Fare fare) {
		return fareRepository.save(fare);
	}



	@Override
	public List<Fare> getAllFare() {
		return fareRepository.findAll();
	}

	

	@Override
	public Fare getFare(int id) throws FareNotFoundException {
		return fareRepository.findById(id).orElseThrow(() -> new FareNotFoundException("Fare Not Found"));
	}

	@Override
	public Fare updateFare(int id, Fare fare) throws FareNotFoundException {
		Fare existing = getFare(id);
		existing.setAmount(fare.getAmount());
		existing.setClassType(fare.getClassType());
		existing.setDestination(fare.getDestination());
		existing.setSource(fare.getSource());
		return fareRepository.save(existing);
	}

	@Override
	public void deleteFare(int id) {
		fareRepository.deleteById(id);
	}

	@Override
	public double getFareByRouteAndCoach(String source, String destination, String classType) {
		Fare f = new Fare();
		f = fareRepository.findBySourceAndDestinationAndClassType(source,destination,classType).orElseThrow(()->new RuntimeException("Fare Not Found"));
		return f.getAmount();
	}

}
