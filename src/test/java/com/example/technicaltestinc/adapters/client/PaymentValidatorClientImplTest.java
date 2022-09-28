package com.example.technicaltestinc.adapters.client;

import com.example.technicaltestinc.dtos.PaymentDTO;
import com.example.technicaltestinc.enums.PaymentType;
import com.example.technicaltestinc.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
class PaymentValidatorClientImplTest {

	@InjectMocks
	private PaymentValidatorClientImpl paymentValidatorClient;

	@Mock
	private RestTemplate restTemplate;

	@Captor
	private ArgumentCaptor<HttpEntity<String>> httpEntityArgumentCaptor;

	@Captor
	private ArgumentCaptor<HttpMethod> httpMethodArgumentCaptor;

	@Captor
	private ArgumentCaptor<String> urlArgumentCaptor;

	private final ObjectMapper objectMapper = new ObjectMapper();


	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField(paymentValidatorClient, "validatorUrl", "http://localhost:9000/payment");
	}

	@Test
	void when_valid_payment_well_formed_rest_call() throws JsonProcessingException {
		// given
		PaymentDTO payment = PaymentDTO.builder()
				.paymentId("a_payment_id")
				.paymentType(PaymentType.ONLINE.label)
				.accountId("an_account_id")
				.amount(300).creditCard("1234-5678")
				.build();

		String payload = new ObjectMapper().writeValueAsString(payment);

		ResponseEntity<String> restResponse = new ResponseEntity<>("Successful", HttpStatus.OK);
		Mockito.doReturn(restResponse).when(restTemplate).exchange(
				urlArgumentCaptor.capture(),
				httpMethodArgumentCaptor.capture(),
				httpEntityArgumentCaptor.capture(),
				eq(String.class));

		// when
		ResponseEntity<String> actualResponse = paymentValidatorClient.validate(payload);

		// then
		assertEquals("Successful", actualResponse.getBody());
		assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
		assertEquals("http://localhost:9000/payment", urlArgumentCaptor.getValue());
		assertEquals(HttpMethod.POST, httpMethodArgumentCaptor.getValue());
		assertEquals("300",
				JsonUtil.retrieveValueFromPath(httpEntityArgumentCaptor.getValue().getBody(), "$.amount"));
		assertEquals("a_payment_id",
				JsonUtil.retrieveValueFromPath(httpEntityArgumentCaptor.getValue().getBody(), "$.payment_id"));
		assertEquals("an_account_id",
				JsonUtil.retrieveValueFromPath(httpEntityArgumentCaptor.getValue().getBody(), "$.account_id"));
		assertEquals("1234-5678",
				JsonUtil.retrieveValueFromPath(httpEntityArgumentCaptor.getValue().getBody(), "$.credit_card"));
		assertEquals("online",
				JsonUtil.retrieveValueFromPath(httpEntityArgumentCaptor.getValue().getBody(), "$.payment_type"));


	}


}