package com.railway.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.railway.entity.Coaches;

public interface CoachRepository extends JpaRepository<Coaches, Integer>{

	List<Coaches> findByTrainTrainId(int trainId);

	int countByTrainTrainIdAndClassType(int trainId, String coachType);

//	List<Coaches> findByTrainTrainIdAndClassType1(int trainId, String classType);

//	@Query("select SUM(c.totalSeats) from Coaches c where c.trainId =: trainId AND c.classType =: classType")
	Coaches findByTrainTrainIdAndClassType(int trainId, String classType);

	Optional<Coaches> findByCoachNumberAndTrainTrainId(String coachNumber, int trainId);


}
