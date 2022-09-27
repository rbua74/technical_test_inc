package com.example.technicaltestinc.service;

import com.example.technicaltestinc.dtos.PaymentDTO;

public interface PaymentProcessor {

	void processOnlinePayment(PaymentDTO payment);

	void processOfflinePayment(PaymentDTO payment);
}
