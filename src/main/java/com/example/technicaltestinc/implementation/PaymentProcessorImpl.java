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
import com.sun.istack.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentProcessorImpl implements PaymentProcessor {

	public static final String ONLINE = "online";
	public static final String OFFLINE = "offline";
	public static final String USER_KAFKA_LISTENER_FACTORY = "userKafkaListenerFactory";
	public static final String PAYMENT_IS_NOT_VALID = "Payment is not valid";
	public static final String ACCOUNT_IS_NOT_VALID = "Account is not valid";

	@Autowired
	private PaymentValidatorClient paymentValidator;

	@Autowired
	private PaymentsRepository paymentsRepository;

	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private PaymentLogsClient paymentLogsClient;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	@KafkaListener(topics = ONLINE, containerFactory = USER_KAFKA_LISTENER_FACTORY)
	public void processOnlinePayment(@NotNull PaymentDTO payment) {
		if (isValid(payment))
			persist(payment);
	}

	@Override
	@KafkaListener(topics = OFFLINE, containerFactory = USER_KAFKA_LISTENER_FACTORY)
	public void processOfflinePayment(@NotNull PaymentDTO payment) {
		persist(payment);
	}

	private void persist(PaymentDTO paymentDTO) {
		Account account = retrieveAccount(paymentDTO);
		if (ONLINE.equals(paymentDTO.getPaymentType())) {
			if (account == null) return;
			account.setLastPaymentDate(LocalDateTime.now().toString());
			accountsRepository.save(account);
		}
		Payment payment = modelMapper.map(paymentDTO, Payment.class);
		payment.setAccount(account);
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

	private boolean isValid(PaymentDTO payment) {
		boolean valid = paymentValidator.validate(payment).getStatusCode().is2xxSuccessful();
		if (!valid) {
			paymentLogsClient.logError(PaymentErrorDTO.builder()
					.paymentId(payment.getPaymentId())
					.errorType(PaymentErrorType.NETWORK.label)
					.errorDescription(PAYMENT_IS_NOT_VALID)
					.build());
		}
		return valid;
	}
}
