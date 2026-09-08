package com.railway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.railway.entity.Coaches;
import com.railway.exception.CoachNotFoundException;
import com.railway.service.CoachService;

@RestController
@RequestMapping("/coach")
public class CoachController {

	@Autowired
	private CoachService coachservice;
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{trainId}")
	public ResponseEntity<String> addCoachDetails(@RequestBody Coaches coach, @PathVariable int trainId){
		coachservice.addCoachDetails(coach,trainId);
		return new ResponseEntity<>("Coach Details Added Successfully..",HttpStatus.OK);
	}
	
	
	
	@PreAuthorize("hasAnyRole('ADMIN','PASSENGER')")
	@GetMapping("/{id}")
	public ResponseEntity<Coaches> getCoach(@PathVariable int id) throws CoachNotFoundException{
		return new ResponseEntity<>(coachservice.getCoaches(id), HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<Coaches>> getAllCoaches(){
		return new ResponseEntity<>(coachservice.getCoaches(),HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<Coaches> update(@PathVariable int id, @RequestBody Coaches coach) throws CoachNotFoundException{
		return new ResponseEntity<>(coachservice.updateCoach(id, coach),HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable int id){
		coachservice.deleteCoach(id);
		return new ResponseEntity<>("Coach details deleted successfully...",HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('PASSENGER')")
	@GetMapping("/train/{trainId}")
	public ResponseEntity<List<Coaches>> getCoachesByTrain(@PathVariable int trainId){
		return new ResponseEntity<>(coachservice.getCoachByTrainId(trainId),HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/count")
	public ResponseEntity<Integer> getCountByType(@RequestParam int trainId, @RequestParam String classType){
		return new ResponseEntity<>(coachservice.getCoachCountByType(trainId, classType), HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('PASSENGER')")
	@GetMapping("/getAvailableSeats")
	public ResponseEntity<Integer> getAvailableSeats(@RequestParam int trainId, @RequestParam String classType){
		return new ResponseEntity<>(coachservice.getAvailableSeatByTrainIdAndClassType(trainId, classType),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('PASSENGER')")
	@PutMapping("/decreaseSeatInCoach")
	public ResponseEntity<String> updateSeatInCoach(@RequestParam int coachId, @RequestParam int updateBySeats) throws CoachNotFoundException{
		coachservice.decreaseSeatInCoach(coachId, updateBySeats);
		return new ResponseEntity<>("Coach Details Updated Successfully...",HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('PASSENGER')")
	@PutMapping("/increaseSeatInCoach")
	public ResponseEntity<String> addSeatInCoach(@RequestParam int coachId, @RequestParam int updateBySeats) throws CoachNotFoundException{
		coachservice.increaseSeatInCoach(coachId, updateBySeats);
		return ResponseEntity.ok("Coach seats are added successfully...");
	}
}
