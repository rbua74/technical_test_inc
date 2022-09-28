package com.example.technicaltestinc.util;

import com.example.technicaltestinc.dtos.PaymentDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ObjectParserUtil {

	private ObjectParserUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static PaymentDTO toPaymentDTO(String payment, ObjectMapper mapper) throws JsonProcessingException {
		return mapper.readValue(payment, PaymentDTO.class);
	}


}
