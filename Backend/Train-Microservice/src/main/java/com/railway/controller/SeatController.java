package com.railway.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.railway.entity.Coaches;
import com.railway.entity.Seat;
import com.railway.entity.SeatIdentifier;
import com.railway.exception.CoachNotFoundException;
import com.railway.exception.ResourceNotFoundException;
import com.railway.exception.SeatNotFoundException;
import com.railway.repository.CoachRepository;
import com.railway.repository.SeatRepository;
import com.railway.service.SeatService;


@RestController
@RequestMapping("/seat")
public class SeatController {
	
	@Autowired
	private SeatService seatService;
	
	@Autowired
	private CoachRepository coachRepo;
	
	@Autowired
	private SeatRepository seatRepository;
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addSeat/{coachId}")
	public ResponseEntity<String> addSeat(@RequestBody Seat seat, @PathVariable int coachId) throws CoachNotFoundException{
		seatService.addSeat(seat, coachId);
		return ResponseEntity.ok("Seat Added Successfully");
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getAllSeats")
	public ResponseEntity<List<Seat>> getAllSeats(){
		return ResponseEntity.ok(seatService.getAllSeats());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/get-seat-by-id/{id}")
	public ResponseEntity<Seat> getSeatById(@PathVariable Long id){
		return ResponseEntity.ok(seatService.getSeatById(id));
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','PASSENGER')")
	@GetMapping("/available/{coachId}")
	public ResponseEntity<List<Seat>> getAvailableSeats(@PathVariable int coachId){
		return ResponseEntity.ok(seatService.getAvailableSeatsByCoachId(coachId));
	}
	
	@PutMapping("/hold")
	public ResponseEntity<Void> holdSeats(@RequestBody List<Long> seatId){
		seatService.holdSeats(seatId);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/mark-booked")
	public ResponseEntity<Void> markBooked(@RequestBody List<Long> seatId){
		seatService.markSeatsAsBooked(seatId);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/confirm-booked")
	public ResponseEntity<Void> confirmBooked(@RequestBody List<Long> seatId){
		seatService.confirmSeatsBooked(seatId);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/mark-available")
	public ResponseEntity<Void> markAvailable(@RequestBody List<Long> seatId){
		seatService.markSeatsAsAvailable(seatId);
		return ResponseEntity.ok().build();
	}
	
	
	@PostMapping("/seat/getSeatIds")
	public ResponseEntity<List<Long>> getSeatIds(@RequestBody List<SeatIdentifier> seatIdentifiers) throws ResourceNotFoundException {
	    List<Long> seatIds = new ArrayList<>();
	    for (SeatIdentifier identifier : seatIdentifiers) {
	        Coaches coach = coachRepo.findByCoachNumberAndTrainTrainId(identifier.getCoachNumber(), identifier.getTrainId())
	            .orElseThrow(() -> new ResourceNotFoundException("Coach not found"));

	        Seat seat = seatRepository.findBySeatNumberAndCoach_CoachId(identifier.getSeatNumber(), coach.getCoachId())
	            .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

	        seatIds.add(seat.getId());
	    }
	    return ResponseEntity.ok(seatIds);
	}

	

}
