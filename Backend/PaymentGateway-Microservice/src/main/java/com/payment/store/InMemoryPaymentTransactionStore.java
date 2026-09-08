package com.payment.store;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import com.payment.entity.PaymentTransactionRecord;

@Component
public class InMemoryPaymentTransactionStore implements PaymentTransactionStore {

	private final ConcurrentHashMap<String, PaymentTransactionRecord> records = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

	@Override
	public ReentrantLock lockFor(String idempotencyKey) {
		return locks.computeIfAbsent(idempotencyKey, key -> new ReentrantLock());
	}

	@Override
	public PaymentTransactionRecord findByIdempotencyKey(String idempotencyKey) {
		return records.get(idempotencyKey);
	}

	@Override
	public PaymentTransactionRecord save(PaymentTransactionRecord record) {
		records.put(record.getIdempotencyKey(), record);
		return record;
	}
}