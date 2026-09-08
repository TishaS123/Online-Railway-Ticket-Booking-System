package com.payment.client;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.payment.dto.TicketStatus;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ReservationStatusClient {

	private final RestTemplate restTemplate;
	private final String reservationBaseUrl;

	public ReservationStatusClient(RestTemplate restTemplate,
			@Value("${reservation.service.base-url:http://localhost:8003}") String reservationBaseUrl) {
		this.restTemplate = restTemplate;
		this.reservationBaseUrl = reservationBaseUrl;
	}

	public void updateTicketStatus(String pnrNo, TicketStatus status) {
		String url = reservationBaseUrl + "/booking/update-status/" + pnrNo + "?status=" + status;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String authorization = currentAuthorizationHeader();
		if (authorization != null && !authorization.isBlank()) {
			headers.set(HttpHeaders.AUTHORIZATION, authorization);
		}

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(headers), String.class);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new IllegalStateException("Failed to update reservation status: " + response.getStatusCode());
		}
	}

	private String currentAuthorizationHeader() {
		if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
			return null;
		}
		HttpServletRequest request = attributes.getRequest();
		return request.getHeader(HttpHeaders.AUTHORIZATION);
	}
}