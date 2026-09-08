package com.railway.service;

import java.util.List;

import com.railway.entity.Coaches;
import com.railway.exception.CoachNotFoundException;

public interface CoachService {
	void addCoachDetails(Coaches coach, int trainId);
	List<Coaches> getCoaches();
	Coaches getCoaches(int id) throws CoachNotFoundException;
	Coaches updateCoach(int id, Coaches coach) throws CoachNotFoundException;
	void deleteCoach(int id);
	List<Coaches> getCoachByTrainId(int trainId);
	int getCoachCountByType(int trainId, String classType);
	int getAvailableSeatByTrainIdAndClassType(int trainId, String classType);
	void decreaseSeatInCoach(int coachId, int updateBySeat) throws CoachNotFoundException;
	void increaseSeatInCoach(int coachId, int addSeats) throws CoachNotFoundException;
}
