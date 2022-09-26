package com.example.technicaltestinc.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PaymentErrorDTO {

	@JsonProperty("payment_id")
	private String paymentId;

	@JsonProperty("error_type")
	private String errorType;

	@JsonProperty("error_description")
	private String errorDescription;
}
