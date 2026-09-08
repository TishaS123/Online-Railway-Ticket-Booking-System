package com.railway.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CoachDTO {
	
	@NotNull(message="Coach Number field is mandatory...")
	@Size(min=2, message="Please provide at least two characters...")
	private String coachNumber;
	@NotNull(message="Class Type field is mandatory...")
	private String classType;
	@NotNull(message="Total Seats field is mandatory...")
	private int totalSeats;
}
