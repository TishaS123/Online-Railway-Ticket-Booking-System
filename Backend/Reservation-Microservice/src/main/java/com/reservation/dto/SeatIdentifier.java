package com.reservation.dto;

import lombok.Data;

@Data
public class SeatIdentifier {
	 private String seatNumber;
	 private String coachNumber;
	 private int trainId;
	    // Getters and setters
	 public String getSeatNumber() {
		 return seatNumber;
	 }
	 public void setSeatNumber(String seatNumber) {
		 this.seatNumber = seatNumber;
	 }
	 public String getCoachNumber() {
		 return coachNumber;
	 }
	 public void setCoachNumber(String coachNumber) {
		 this.coachNumber = coachNumber;
	 }
	 public int getTrainId() {
		 return trainId;
	 }
	 public void setTrainId(int trainId) {
		 this.trainId = trainId;
	 }
	 
	 
}
