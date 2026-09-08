package com.railway.service;

import java.util.List;

import com.railway.entity.Fare;
import com.railway.exception.FareNotFoundException;

public interface FareService {
	Fare addFareDetails(Fare fare);
	Fare getFare(int id) throws FareNotFoundException;
	List<Fare> getAllFare();
	Fare updateFare(int id, Fare fare) throws FareNotFoundException;
	void deleteFare(int id);
	double getFareByRouteAndCoach(String source, String destination, String classType);
}
