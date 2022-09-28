package com.example.technicaltestinc.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface PaymentProcessor {

	void processOnlinePayment(String payment) throws JsonProcessingException;

	void processOfflinePayment(String payment) throws JsonProcessingException;
}
