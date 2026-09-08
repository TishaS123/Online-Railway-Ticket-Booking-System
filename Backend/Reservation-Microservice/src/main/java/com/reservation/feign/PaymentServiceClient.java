package com.reservation.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.reservation.dto.PaymentRequest;
import com.reservation.dto.PaymentResponse;

import jakarta.annotation.PostConstruct;

@FeignClient(name="PaymentGateway", url="http://localhost:8005/api/payments", configuration = FeignClientConfig.class)
public interface PaymentServiceClient {
	
	@PostMapping("/charge")
	ResponseEntity<PaymentResponse> createCharge(@RequestBody PaymentRequest request);

}
