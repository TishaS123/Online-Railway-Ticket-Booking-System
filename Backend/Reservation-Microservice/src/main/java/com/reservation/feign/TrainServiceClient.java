package com.reservation.feign;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.reservation.dto.SeatIdentifier;
import com.reservation.dto.Train;
import com.reservation.entity.Seat;


@FeignClient(name="Train-Microservice", url="http://localhost:8002", configuration = FeignClientConfig.class)
public interface TrainServiceClient {

	@GetMapping("/fare/getFareByRouteAndClassType")
	double getFareByRouteAndType(@RequestHeader("Authorization") String authorization, @RequestParam String source, @RequestParam String destination, @RequestParam String classType);
	
	
	@GetMapping("/train/getTrainByIdSourceDestinationDate")
	Train getTrainByIdSourceDestinationDate(@RequestHeader("Authorization") String authorization, @RequestParam int trainId, @RequestParam String source, @RequestParam String destination, @RequestParam String date);

	
	@GetMapping("/seat/available/{coachId}")
	List<Seat> getAvailableSeatsByCoachId(@RequestHeader("Authorization") String authorization, @PathVariable int coachId);
	
	@PutMapping("/seat/hold")
	void holdSeats(@RequestHeader("Authorization") String authorization, @RequestBody List<Long> seatIds);
	
	@PutMapping("/seat/mark-booked")
	void markSeatAsBooked(@RequestHeader("Authorization") String authorization, @RequestBody List<Long> seatIds);
	
	@PutMapping("/seat/confirm-booked")
	void confirmSeatsBooked(@RequestHeader("Authorization") String authorization, @RequestBody List<Long> seatIds);
	
	@PutMapping("/seat/mark-available")
	void markSeatAsAvailable(@RequestHeader("Authorization") String authorization, @RequestBody List<Long> seatIds);
	

	@PostMapping("/seat/seat/getSeatIds")
	List<Long> getSeatIds(@RequestHeader("Authorization") String authorization, @RequestBody List<SeatIdentifier> seatIdentifiers);
	
	

}
