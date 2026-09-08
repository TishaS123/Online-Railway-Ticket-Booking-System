package com.reservation.dto;

import java.time.LocalDate;
import java.util.List;

import com.reservation.entity.Passenger;

import java.util.List;

import lombok.Data;

@Data
public class TicketRequest {
	private int trainId;
	private String source;
	private String destination;
	private String classType;
	private LocalDate date;
	private String idempotencyKey;
	private List<Passenger> passengers;
	public int getTrainId() {
		return trainId;
	}
	public void setTrainId(int trainId) {
		this.trainId = trainId;
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
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getIdempotencyKey() {
		return idempotencyKey;
	}
	public void setIdempotencyKey(String idempotencyKey) {
		this.idempotencyKey = idempotencyKey;
	}
	public List<Passenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
	
	
}
