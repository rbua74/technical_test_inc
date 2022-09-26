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
public class PaymentDTO {

	@JsonProperty("payment_id")
	private String paymentId;

	@JsonProperty("account_id")
	private String accountId;

	@JsonProperty("payment_type")
	private String paymentType;

	@JsonProperty("credit_card")
	private String creditCard;

	private int amount;

}
