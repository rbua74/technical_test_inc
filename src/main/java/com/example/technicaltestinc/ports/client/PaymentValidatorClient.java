package com.example.technicaltestinc.ports.client;

import org.springframework.http.ResponseEntity;

public interface PaymentValidatorClient {

	ResponseEntity<String> validate(String payment);

}
