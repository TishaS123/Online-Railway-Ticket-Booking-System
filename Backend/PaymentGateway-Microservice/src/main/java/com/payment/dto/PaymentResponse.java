package com.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PaymentResponse {
	private String status;
	private String chargeId;
	private String message;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public PaymentResponse(String status, String chargeId) {
		super();
		this.status = status;
		this.chargeId = chargeId;
	}

	public PaymentResponse(String status, String chargeId, String message) {
		super();
		this.status = status;
		this.chargeId = chargeId;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
