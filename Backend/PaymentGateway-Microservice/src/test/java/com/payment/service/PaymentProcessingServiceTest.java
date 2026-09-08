package com.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.payment.dto.PaymentRequest;
import com.payment.dto.PaymentResponse;
import com.payment.entity.PaymentTransactionRecord;
import com.payment.entity.PaymentTransactionStatus;
import com.payment.store.InMemoryPaymentTransactionStore;
import com.payment.store.PaymentTransactionStore;

@ExtendWith(MockitoExtension.class)
class PaymentProcessingServiceTest {

	@Mock
	private StripeChargeClient stripeChargeClient;

	private PaymentProcessingService paymentProcessingService;

	private PaymentTransactionStore paymentTransactionStore;

	@BeforeEach
	void setUp() {
		paymentProcessingService = new PaymentProcessingService();
		paymentTransactionStore = new InMemoryPaymentTransactionStore();
		ReflectionTestUtils.setField(paymentProcessingService, "paymentTransactionStore", paymentTransactionStore);
		ReflectionTestUtils.setField(paymentProcessingService, "stripeChargeClient", stripeChargeClient);
	}

	@Test
	void createCharge_shouldReturnCachedResponseForDuplicateIdempotencyKey() {
		PaymentTransactionRecord existing = new PaymentTransactionRecord();
		existing.setIdempotencyKey("booking-123");
		existing.setPnrNo("PNR001");
		existing.setStatus(PaymentTransactionStatus.SUCCEEDED);
		existing.setChargeId("ch_123");
		paymentTransactionStore.save(existing);

		PaymentRequest request = new PaymentRequest();
		request.setIdempotencyKey("booking-123");
		request.setPnrNo("PNR001");

		PaymentResponse response = paymentProcessingService.createCharge(request);

		assertEquals("succeeded", response.getStatus());
		assertEquals("ch_123", response.getChargeId());
		verifyNoInteractions(stripeChargeClient);
	}
}
