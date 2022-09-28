package com.example.technicaltestinc.adapters.client;

import com.example.technicaltestinc.ports.client.PaymentValidatorClient;
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
public class PaymentValidatorClientImpl implements PaymentValidatorClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${payments.validator.url}")
	private String validatorUrl;


	@Override
	public ResponseEntity<String> validate(String payment) {
		HttpEntity<String> entity = new HttpEntity<>(payment, buildHeaders());
		return restTemplate.exchange(
				validatorUrl,
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

