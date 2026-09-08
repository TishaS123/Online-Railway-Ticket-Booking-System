package com.reservation.dto;

import lombok.Data;

@Data
public class PaymentResponse {
	private String status;
	private String chargeId;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChargeId() {
		return chargeId;
	}
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	
	
}
