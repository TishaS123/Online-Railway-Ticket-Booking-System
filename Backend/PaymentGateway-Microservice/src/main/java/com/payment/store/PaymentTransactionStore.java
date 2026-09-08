package com.payment.store;

import java.util.concurrent.locks.ReentrantLock;

import com.payment.entity.PaymentTransactionRecord;

public interface PaymentTransactionStore {

	ReentrantLock lockFor(String idempotencyKey);

	PaymentTransactionRecord findByIdempotencyKey(String idempotencyKey);

	PaymentTransactionRecord save(PaymentTransactionRecord record);
}