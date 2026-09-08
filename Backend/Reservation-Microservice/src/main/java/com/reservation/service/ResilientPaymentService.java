package com.reservation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.reservation.dto.PaymentRequest;
import com.reservation.dto.PaymentResponse;
import com.reservation.exception.PaymentServiceUnavailableException;
import com.reservation.feign.PaymentServiceClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ResilientPaymentService {

    private static final Logger log =
            LoggerFactory.getLogger(ResilientPaymentService.class);

    private final PaymentServiceClient paymentServiceClient;

    public ResilientPaymentService(
            PaymentServiceClient paymentServiceClient) {

        this.paymentServiceClient = paymentServiceClient;
    }

    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public ResponseEntity<PaymentResponse> createCharge(
            PaymentRequest request) {

        return paymentServiceClient.createCharge(request);
    }

    private ResponseEntity<PaymentResponse> paymentFallback(
            PaymentRequest request,
            Throwable throwable) {

        log.error(
                "Payment Gateway unavailable. PNR={}, idempotencyKey={}",
                request.getPnrNo(),
                request.getIdempotencyKey(),
                throwable
        );

        throw new PaymentServiceUnavailableException(
                "Payment service is temporarily unavailable. Please try again later."
        );
    }
}