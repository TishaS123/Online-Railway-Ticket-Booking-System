package com.railway.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter@Getter
@NoArgsConstructor
public class Coaches {
	@Id
	@GeneratedValue
	private int coachId;
	private String coachNumber;
	private String classType;
	private int totalSeats;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="train_id")
	private Train train;
	
	
	@JsonManagedReference
	@OneToMany(mappedBy="coach", cascade = CascadeType.ALL)
	private List<Seat> seats;


	public int getCoachId() {
		return coachId;
	}


	public void setCoachId(int coachId) {
		this.coachId = coachId;
	}


	public String getCoachNumber() {
		return coachNumber;
	}


	public void setCoachNumber(String coachNumber) {
		this.coachNumber = coachNumber;
	}


	public String getClassType() {
		return classType;
	}


	public void setClassType(String classType) {
		this.classType = classType;
	}


	public int getTotalSeats() {
		return totalSeats;
	}


	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}


	public Train getTrain() {
		return train;
	}


	public void setTrain(Train train) {
		this.train = train;
	}


	public List<Seat> getSeats() {
		return seats;
	}


	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}
	
	
}
