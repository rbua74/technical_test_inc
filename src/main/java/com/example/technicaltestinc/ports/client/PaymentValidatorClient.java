package com.example.technicaltestinc.ports.client;

import com.example.technicaltestinc.dtos.PaymentDTO;
import org.springframework.http.ResponseEntity;

public interface PaymentValidatorClient {

	ResponseEntity<String> validate(PaymentDTO payment);

}
