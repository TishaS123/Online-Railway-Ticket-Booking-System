package com.railway.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TrainDTO {
	
	@NotNull(message="Train name field is mandatory...")
	private String trainName;
	
	@NotNull(message="Source field is mandatory...")
	private String source;
	
	@NotNull(message="Destination fiels is mandatory..")
	private String destination;
	
	@NotNull(message="Arrival Time field is mandatory...")
	private LocalTime arrivalTime;
	
	@NotNull(message="Departure Time field is mandatory...")
	private LocalTime departureTime;
	
	@NotNull(message="Date field is mandatory...")
	@Future
	private LocalDate date;
}
