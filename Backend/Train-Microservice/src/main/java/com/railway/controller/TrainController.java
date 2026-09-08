package com.railway.controller;

import java.time.LocalDate;
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

import com.railway.entity.Train;
import com.railway.service.TrainService;

@RestController
@RequestMapping("/train")
public class TrainController {
	
	@Autowired
	private TrainService trainService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/addTrainDetails")
	public ResponseEntity<String> addTrainDetails(@RequestBody Train train){
		trainService.addTrainDetails(train);
		return new ResponseEntity<>("Train Details are added successfully...", HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','PASSENGER')")
	@GetMapping("/{trainId}")
	public ResponseEntity<Train> getTrainByTrainId(@PathVariable int trainId){
		return new ResponseEntity<>(trainService.getTrainById(trainId),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','PASSENGER')")
	@GetMapping
	public ResponseEntity<List<Train>> getAllTrains(){
		return new ResponseEntity<>(trainService.getAllTrain(),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('PASSENGER')")
	@GetMapping("/{source}/{destination}")
	public ResponseEntity<List<Train>> getAllTrainsFromSourceAndDestination(@PathVariable String source, @PathVariable String destination){
		return new ResponseEntity<>(trainService.getTrainBySourceAndDestination(source, destination),HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN','PASSENGER')")
	@GetMapping("/search")
	public ResponseEntity<List<Train>> searchTrains(@RequestParam String source,
			@RequestParam String destination,
			@RequestParam String classType,
			@RequestParam LocalDate date){
		List<Train> trains = trainService.getTrainBySourceAndDestination(source, destination);
		List<Train> filtered = trains.stream()
			.filter(train -> train.getDate() != null && train.getDate().equals(date))
			.filter(train -> train.getCoaches() != null && train.getCoaches().stream()
				.anyMatch(coach -> coach.getClassType() != null && coach.getClassType().equalsIgnoreCase(classType)))
			.toList();
		return new ResponseEntity<>(filtered, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<String> updateTrainDetails(@PathVariable int id, Train train){
		trainService.updateTrain(id, train);
		return new ResponseEntity<>("Train Details Updated Successfully...", HttpStatus.OK);
	}
	

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable int id){
		trainService.delete(id);
		return new ResponseEntity<>("Train Details Deleted Successfully.", HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('PASSENGER')")
	@GetMapping("/getTrainByIdSourceDestinationDate")
	public ResponseEntity<Train> getTrainByIdSourceDestinationDate(@RequestParam int trainId, @RequestParam String source, @RequestParam String destination, @RequestParam LocalDate date){
		return new ResponseEntity<>(trainService.getTrainByIdAndSourceAndDestinationAndDate(trainId, source, destination, date),HttpStatus.OK);
	}
	
}
