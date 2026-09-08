package com.railway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.railway.entity.Coaches;
import com.railway.entity.Train;
import com.railway.exception.CoachNotFoundException;
import com.railway.feign.ReservationClient;
import com.railway.repository.CoachRepository;

@Service
public class CoachServiceImpl implements CoachService {

	@Autowired
	private ReservationClient reservationClient;
	
	@Autowired
	private CoachRepository coachRepo;
	
	@Autowired
	private TrainService service;
	

	
	@Override
	public void addCoachDetails(Coaches coach, int trainId) {
		Train train = service.getTrainById(trainId);
		coach.setTrain(train);
		coachRepo.save(coach);
	}


	@Override
	public List<Coaches> getCoaches() {
		return coachRepo.findAll();
	}

	@Override
	public Coaches getCoaches(int id) throws CoachNotFoundException {
		return coachRepo.findById(id).orElseThrow(() -> new CoachNotFoundException("Coach Not Found with Id : "+id));
	}

	@Override
	public Coaches updateCoach(int id, Coaches coach) throws CoachNotFoundException {
		Coaches existing = getCoaches(id);
		existing.setCoachNumber(coach.getCoachNumber());
		existing.setClassType(coach.getClassType());
		existing.setTotalSeats(coach.getTotalSeats());
		existing.setTrain(coach.getTrain());
		return coachRepo.save(existing);
	}

	@Override
	public void deleteCoach(int id) {
		coachRepo.deleteById(id);
	}

	@Override
	public List<Coaches> getCoachByTrainId(int trainId) {
		return coachRepo.findByTrainTrainId(trainId);
	}

	@Override
	public int getCoachCountByType(int trainId, String classType) {
		return coachRepo.countByTrainTrainIdAndClassType(trainId, classType);
	}


	@Override
	public int getAvailableSeatByTrainIdAndClassType(int trainId, String classType) {
		
		Coaches coaches = coachRepo.findByTrainTrainIdAndClassType(trainId, classType);

		return coaches.getTotalSeats();
	}


	@Override
	public void decreaseSeatInCoach(int coachId, int updateBySeat) throws CoachNotFoundException {
		Coaches coach = coachRepo.findById(coachId).orElseThrow(() -> new CoachNotFoundException("Coach Not Found with Id: "+coachId));
		if(coach != null) {
			int availableSeats = coach.getTotalSeats() - updateBySeat;
			coach.setTotalSeats(availableSeats);
			coachRepo.save(coach);
		}
		
	}


	@Override
	public void increaseSeatInCoach(int coachId, int addSeats) throws CoachNotFoundException {
		Coaches coach = coachRepo.findById(coachId).orElseThrow(() -> new CoachNotFoundException("Coach Not Found with Id: "+coachId));
		if(coach != null) {
			int updatedSeats = coach.getTotalSeats() + addSeats;
			coach.setTotalSeats(updatedSeats);
			coachRepo.save(coach);
		}
	}


	

}
