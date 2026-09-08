package com.payment.service;

import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.payment.dto.PaymentRequest;
import com.payment.dto.PaymentResponse;
import com.payment.entity.PaymentTransactionRecord;
import com.payment.entity.PaymentTransactionStatus;
import com.payment.store.PaymentTransactionStore;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

@Service
public class PaymentProcessingService {

	private static final int PROCESSING_WAIT_ATTEMPTS = 150;
	private static final long PROCESSING_WAIT_MILLIS = 200L;

	@Autowired
	private PaymentTransactionStore paymentTransactionStore;

	@Autowired
	private StripeChargeClient stripeChargeClient;

	public PaymentResponse createCharge(PaymentRequest request) {
		String idempotencyKey = resolveIdempotencyKey(request);
		ReentrantLock lock = paymentTransactionStore.lockFor(idempotencyKey);
		lock.lock();
		try {
			PaymentTransactionRecord existing = paymentTransactionStore.findByIdempotencyKey(idempotencyKey);
			if (existing != null && existing.getStatus() != PaymentTransactionStatus.PROCESSING) {
				return toResponse(existing);
			}

			PaymentTransactionRecord transaction = existing;
			if (transaction == null) {
				transaction = new PaymentTransactionRecord();
				transaction.setCreatedAt(Instant.now());
			}

			transaction.setIdempotencyKey(idempotencyKey);
			transaction.setPnrNo(request.getPnrNo());
			transaction.setAmount(request.getAmount());
			transaction.setCurrency(request.getCurrency());
			transaction.setDescription(request.getDescription());
			transaction.setEmail(request.getEmail());
			transaction.setStatus(PaymentTransactionStatus.PROCESSING);
			transaction.setUpdatedAt(Instant.now());
			paymentTransactionStore.save(transaction);

			try {
				Charge charge = stripeChargeClient.createCharge(request, idempotencyKey);
				PaymentTransactionRecord completed = markSucceeded(idempotencyKey, charge.getId());
				return toResponse(completed);
			} catch (StripeException ex) {
				PaymentTransactionRecord failed = markFailed(idempotencyKey, ex.getMessage());
				return new PaymentResponse("failed", failed.getChargeId(), failed.getFailureReason());
			}
		} finally {
			lock.unlock();
		}
	}

	private PaymentTransactionRecord markSucceeded(String idempotencyKey, String chargeId) {
		PaymentTransactionRecord transaction = paymentTransactionStore.findByIdempotencyKey(idempotencyKey);
		if (transaction == null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Payment transaction missing after charge creation");
		}
		transaction.setChargeId(chargeId);
		transaction.setStatus(PaymentTransactionStatus.SUCCEEDED);
		transaction.setFailureReason(null);
		transaction.setUpdatedAt(Instant.now());
		return paymentTransactionStore.save(transaction);
	}

	private PaymentTransactionRecord markFailed(String idempotencyKey, String failureReason) {
		PaymentTransactionRecord transaction = paymentTransactionStore.findByIdempotencyKey(idempotencyKey);
		if (transaction == null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Payment transaction missing after charge failure");
		}
		transaction.setStatus(PaymentTransactionStatus.FAILED);
		transaction.setFailureReason(failureReason);
		transaction.setUpdatedAt(Instant.now());
		return paymentTransactionStore.save(transaction);
	}

	private PaymentResponse toResponse(PaymentTransactionRecord transaction) {
		return new PaymentResponse(
				transaction.getStatus() == PaymentTransactionStatus.SUCCEEDED ? "succeeded" : "failed",
				transaction.getChargeId(), transaction.getFailureReason());
	}

	private String resolveIdempotencyKey(PaymentRequest request) {
		if (request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()) {
			return request.getIdempotencyKey();
		}
		if (request.getPnrNo() != null && !request.getPnrNo().isBlank()) {
			return request.getPnrNo();
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
				"idempotencyKey or pnrNo is required for payment processing");
	}
}