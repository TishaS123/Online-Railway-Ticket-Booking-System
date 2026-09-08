package com.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.dto.PaymentRequest;
import com.payment.dto.PaymentResponse;
import com.payment.service.PaymentProcessingService;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	@Autowired
	private PaymentProcessingService paymentProcessingService;
	
	
	
	@PreAuthorize("hasRole('PASSENGER')")
	@PostMapping("/charge")
	public ResponseEntity<PaymentResponse> createCharge(@RequestBody PaymentRequest request){
		return ResponseEntity.ok(paymentProcessingService.createCharge(request));
	}
}