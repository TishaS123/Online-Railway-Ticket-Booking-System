package com.payment.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.payment.dto.PaymentRequest;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;

@Component
public class StripeChargeClient {

	public Charge createCharge(PaymentRequest request, String idempotencyKey) throws StripeException {
		Map<String, Object> chargeParams = new HashMap<>();
		chargeParams.put("amount", request.getAmount());
		chargeParams.put("currency", request.getCurrency());
		chargeParams.put("description", request.getDescription());
		chargeParams.put("receipt_email", request.getEmail());
		chargeParams.put("source", "tok_visa");

		RequestOptions requestOptions = RequestOptions.builder().setIdempotencyKey(idempotencyKey).build();
		return Charge.create(chargeParams, requestOptions);
	}
}