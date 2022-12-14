package com.example.technicaltestinc.adapters.client;

import com.example.technicaltestinc.dtos.PaymentErrorDTO;
import com.example.technicaltestinc.enums.PaymentErrorType;
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
class PaymentLogsClientImplTest {

	@InjectMocks
	private PaymentLogsClientImpl paymentLogsClient;

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
		ReflectionTestUtils.setField(paymentLogsClient, "logsUrl", "http://localhost:9000/payment");
	}

	@Test
	void when_valid_payment_well_formed_rest_call() throws JsonProcessingException {
		// given
		PaymentErrorDTO paymentError = PaymentErrorDTO.builder()
				.paymentId("a_payment_id")
				.errorType(PaymentErrorType.DATABASE.label)
				.errorDescription("a SQL Exception occurred")
				.build();

		ResponseEntity<String> restResponse = new ResponseEntity<>("Successful", HttpStatus.OK);
		Mockito.doReturn(restResponse).when(restTemplate).exchange(
				urlArgumentCaptor.capture(),
				httpMethodArgumentCaptor.capture(),
				httpEntityArgumentCaptor.capture(),
				eq(String.class));

		// when
		ResponseEntity<String> actualResponse = paymentLogsClient.logError(paymentError);

		// then
		assertEquals("Successful", actualResponse.getBody());
		assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
		assertEquals("http://localhost:9000/payment", urlArgumentCaptor.getValue());
		assertEquals(HttpMethod.POST, httpMethodArgumentCaptor.getValue());
		assertEquals("a_payment_id",
				JsonUtil.retrieveValueFromPath(objectMapper.writeValueAsString(httpEntityArgumentCaptor.getValue().getBody()), "$.payment_id"));
		assertEquals("database",
				JsonUtil.retrieveValueFromPath(objectMapper.writeValueAsString(httpEntityArgumentCaptor.getValue().getBody()), "$.error_type"));
		assertEquals("a SQL Exception occurred",
				JsonUtil.retrieveValueFromPath(objectMapper.writeValueAsString(httpEntityArgumentCaptor.getValue().getBody()), "$.error_description"));

	}


}