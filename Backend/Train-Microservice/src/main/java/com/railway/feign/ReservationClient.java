package com.railway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="Reservation-Service", url="http://localhost:8003")
public interface ReservationClient {

	@GetMapping("/booking/booked-seats")
	int getBookedSeats(@RequestParam int trainId, @RequestParam String classType);
	
	
}
