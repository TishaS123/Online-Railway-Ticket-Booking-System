package com.railway.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.railway.entity.Fare;
import com.railway.exception.FareNotFoundException;
import com.railway.service.FareService;

@RestController
@RequestMapping("/fare")
public class FareController {
	
	@Autowired
	private FareService fareservice;
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addFareDetails")
	public ResponseEntity<String> addfareDetails(@RequestBody Fare fare){
		fareservice.addFareDetails(fare);
		return new ResponseEntity<>("Fare Details added successfully..", HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<Fare> getFare(@PathVariable int id) throws FareNotFoundException {
		return new ResponseEntity<>(fareservice.getFare(id),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','PASSENGER')")
	@GetMapping
	public ResponseEntity<List<Fare>> getAllFare(){
		return new ResponseEntity<>(fareservice.getAllFare(),HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<String> updateFare(@PathVariable int id, @RequestBody Fare fare) throws FareNotFoundException{
		fareservice.updateFare(id, fare);
		return new ResponseEntity<>("Fare Details Updated Successfully...",HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteFare(@PathVariable int id){
		fareservice.deleteFare(id);
		return new ResponseEntity<>("Fare Details Deleted Successfully...",HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('PASSENGER')")
	@GetMapping("/getFareByRouteAndClassType")
	public ResponseEntity<Double> getFareByRouteAndType(@RequestParam("source") String source, @RequestParam("destination") String destination, @RequestParam("classType") String classType){
		return new ResponseEntity<>(fareservice.getFareByRouteAndCoach(source, destination, classType),HttpStatus.OK);
	}
}
