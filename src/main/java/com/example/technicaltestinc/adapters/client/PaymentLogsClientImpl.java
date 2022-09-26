package com.example.technicaltestinc.adapters.client;

import com.example.technicaltestinc.dtos.PaymentErrorDTO;
import com.example.technicaltestinc.ports.client.PaymentLogsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentLogsClientImpl implements PaymentLogsClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${payments.logs.url}")
	private String logsUrl;


	@Override
	public ResponseEntity<String> logError(PaymentErrorDTO paymentError) {
		HttpEntity<PaymentErrorDTO> entity = new HttpEntity<>(paymentError, buildHeaders());
		return restTemplate.exchange(
				logsUrl,
				HttpMethod.POST,
				entity,
				String.class);
	}


	private HttpHeaders buildHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}
}
