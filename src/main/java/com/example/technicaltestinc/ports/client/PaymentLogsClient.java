package com.example.technicaltestinc.ports.client;

import com.example.technicaltestinc.dtos.PaymentErrorDTO;
import org.springframework.http.ResponseEntity;

public interface PaymentLogsClient {

	ResponseEntity<String> logError(PaymentErrorDTO paymentError);
}
