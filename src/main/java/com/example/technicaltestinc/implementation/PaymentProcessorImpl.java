package com.example.technicaltestinc.implementation;

import com.example.technicaltestinc.dtos.PaymentDTO;
import com.example.technicaltestinc.dtos.PaymentErrorDTO;
import com.example.technicaltestinc.entities.Account;
import com.example.technicaltestinc.entities.Payment;
import com.example.technicaltestinc.enums.PaymentErrorType;
import com.example.technicaltestinc.ports.client.PaymentLogsClient;
import com.example.technicaltestinc.ports.client.PaymentValidatorClient;
import com.example.technicaltestinc.ports.repository.AccountsRepository;
import com.example.technicaltestinc.ports.repository.PaymentsRepository;
import com.example.technicaltestinc.service.PaymentProcessor;
import com.example.technicaltestinc.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.technicaltestinc.util.ModelMapperUtil.map;
import static com.example.technicaltestinc.util.ObjectParserUtil.toPaymentDTO;

@Service
public class PaymentProcessorImpl implements PaymentProcessor {

	public static final String ONLINE = "online";
	public static final String OFFLINE = "offline";
	public static final String PAYMENT_IS_NOT_VALID = "Payment is not valid";
	public static final String ACCOUNT_IS_NOT_VALID = "Account is not valid";
	public static final String PAYMENT_ID_PATH = "$.payment_id";

	@Autowired
	private PaymentValidatorClient paymentValidator;

	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private PaymentLogsClient paymentLogsClient;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	@KafkaListener(topics = ONLINE)
	public void processOnlinePayment(String payment) throws JsonProcessingException {
		if (isValid(payment))
			persist(toPaymentDTO(payment, objectMapper));
	}

	@Override
	@KafkaListener(topics = OFFLINE)
	public void processOfflinePayment(String payment) throws JsonProcessingException {
		persist(toPaymentDTO(payment, objectMapper));
	}


	private void persist(PaymentDTO paymentDTO) {
		Account account = retrieveAccount(paymentDTO);
		if (ONLINE.equals(paymentDTO.getPaymentType())) {
			if (account == null) return;
			account.setLastPaymentDate(Timestamp.valueOf(LocalDateTime.now()));
			accountsRepository.save(account);
		}
		Payment payment = map(paymentDTO);
		payment.setAccount(account);
		payment.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
		paymentsRepository.save(payment);

	}

	private Account retrieveAccount(PaymentDTO payment) {
		if (payment.getAccountId() == null) return null;
		Optional<Account> optionalAccount = accountsRepository.findById(Integer.parseInt(payment.getAccountId()));
		if (optionalAccount == null || optionalAccount.isEmpty()) {
			paymentLogsClient.logError(PaymentErrorDTO.builder()
					.paymentId(payment.getPaymentId())
					.errorType(PaymentErrorType.DATABASE.label)
					.errorDescription(ACCOUNT_IS_NOT_VALID)
					.build());
			return null;
		} else {
			return optionalAccount.get();
		}
	}

	private boolean isValid(String payment) {
		boolean valid = paymentValidator.validate(payment).getStatusCode().is2xxSuccessful();
		if (!valid) {
			paymentLogsClient.logError(PaymentErrorDTO.builder()
					.paymentId(JsonUtil.retrieveValueFromPath(payment, PAYMENT_ID_PATH))
					.errorType(PaymentErrorType.NETWORK.label)
					.errorDescription(PAYMENT_IS_NOT_VALID)
					.build());
		}
		return valid;
	}
}
