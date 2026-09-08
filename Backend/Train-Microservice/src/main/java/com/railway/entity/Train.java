package com.railway.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author Tisha Sorte
 * @version ORTB 1.0
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Train {
	
	@Id
	@GeneratedValue
	private int trainId;
	private String trainName;
	private String source;
	private String destination;
	private LocalTime arrivalTime;
	private LocalTime departureTime;
	private LocalDate date;
	
	
	@JsonManagedReference
	@OneToMany(mappedBy="train", cascade=CascadeType.ALL)
	private List<Coaches> coaches;


	public int getTrainId() {
		return trainId;
	}


	public void setTrainId(int trainId) {
		this.trainId = trainId;
	}


	public String getTrainName() {
		return trainName;
	}


	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}


	public String getSource() {
		return source;
	}


	public void setSource(String source) {
		this.source = source;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}


	public LocalTime getArrivalTime() {
		return arrivalTime;
	}


	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public LocalTime getDepartureTime() {
		return departureTime;
	}


	public void setDepartureTime(LocalTime departureTime) {
		this.departureTime = departureTime;
	}


	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate date) {
		this.date = date;
	}


	public List<Coaches> getCoaches() {
		return coaches;
	}


	public void setCoaches(List<Coaches> coaches) {
		this.coaches = coaches;
	}
	
	
	
}
