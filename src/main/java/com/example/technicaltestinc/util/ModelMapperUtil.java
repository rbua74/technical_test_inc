package com.example.technicaltestinc.util;

import com.example.technicaltestinc.dtos.PaymentDTO;
import com.example.technicaltestinc.entities.Account;
import com.example.technicaltestinc.entities.Payment;
import com.example.technicaltestinc.enums.PaymentType;

public class ModelMapperUtil {

	public static final String ONLINE = "online";

	private ModelMapperUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static PaymentDTO map(Payment payment) {
		return PaymentDTO.builder()
				.paymentId(payment.getPaymentId())
				.paymentType(payment.getPaymentType().label)
				.amount(payment.getAmount())
				.creditCard(payment.getCreditCard())
				.accountId(String.valueOf(payment.getAccount().getAccountId()))
				.build();

	}

	public static Payment map(PaymentDTO paymentDTO) {
		Account account = Account.builder().accountId(Integer.parseInt(paymentDTO.getAccountId())).build();
		return Payment.builder()
				.paymentId(paymentDTO.getPaymentId())
				.paymentType(ONLINE.equalsIgnoreCase(paymentDTO.getPaymentType()) ? PaymentType.ONLINE : PaymentType.OFFLINE)
				.amount(paymentDTO.getAmount())
				.creditCard(paymentDTO.getCreditCard())
				.account(account)
				.build();

	}
}
