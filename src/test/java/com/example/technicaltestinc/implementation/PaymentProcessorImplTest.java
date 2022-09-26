package com.example.technicaltestinc.implementation;

import com.example.technicaltestinc.dtos.PaymentDTO;
import com.example.technicaltestinc.dtos.PaymentErrorDTO;
import com.example.technicaltestinc.entities.Account;
import com.example.technicaltestinc.entities.Payment;
import com.example.technicaltestinc.enums.PaymentType;
import com.example.technicaltestinc.ports.client.PaymentLogsClient;
import com.example.technicaltestinc.ports.client.PaymentValidatorClient;
import com.example.technicaltestinc.ports.repository.AccountsRepository;
import com.example.technicaltestinc.ports.repository.PaymentsRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorImplTest {

	@InjectMocks
	private PaymentProcessorImpl paymentProcessor;

	@Mock
	private PaymentValidatorClient paymentValidator;

	@Mock
	private PaymentsRepository paymentsRepository;

	@Mock
	private AccountsRepository accountsRepository;

	@Mock
	private PaymentLogsClient paymentLogsClient;

	@Spy
	private ModelMapper modelMapper;

	@Captor
	private ArgumentCaptor<PaymentErrorDTO> paymentErrorDTOArgumentCaptor;

	@Before
	void setup() {


	}

	@Test
	void when_offline_valid_payment_returns_success() {

		// given
		PaymentDTO paymentDTO = aPaymentDTO();

		Account account = anAccount();
		assert account != null;
		Optional<Account> optional = Optional.of(account);

		doCallRealMethod().when(modelMapper).map(paymentDTO, Payment.class);
		when(paymentValidator.validate(paymentDTO)).thenReturn(new ResponseEntity<>("Successful", HttpStatus.OK));
		when(accountsRepository.findById(123)).thenReturn(optional);

		// when
		paymentProcessor.processOnlinePayment(paymentDTO);

		// then
		verify(paymentValidator).validate(paymentDTO);
		verify(accountsRepository).findById(123);
		verify(accountsRepository).save(account);
		verify(paymentsRepository).save(any(Payment.class));

	}

	@Test
	void when_offline_valid_payment_validation_is_not_called() {

		// given
		PaymentDTO paymentDTO = aPaymentDTO();
		paymentDTO.setPaymentType(PaymentType.OFFLINE.label);

		Account account = anAccount();
		assert account != null;
		Optional<Account> optional = Optional.of(account);

		doCallRealMethod().when(modelMapper).map(paymentDTO, Payment.class);

		when(accountsRepository.findById(123)).thenReturn(optional);

		// when
		paymentProcessor.processOfflinePayment(paymentDTO);

		// then
		verify(paymentValidator, times(0)).validate(paymentDTO);

	}

	@Test
	void when_invalid_online_payment_validation_log_is_sent() {

		// given
		PaymentDTO paymentDTO = aPaymentDTO();

		when(paymentValidator.validate(paymentDTO)).thenReturn(new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST));

		// when
		paymentProcessor.processOnlinePayment(paymentDTO);

		// then
		verify(paymentLogsClient, times(1)).logError(paymentErrorDTOArgumentCaptor.capture());
		assertEquals("a_payment_id", paymentErrorDTOArgumentCaptor.getValue().getPaymentId());
		assertEquals("network", paymentErrorDTOArgumentCaptor.getValue().getErrorType());
		assertEquals("Payment is not valid", paymentErrorDTOArgumentCaptor.getValue().getErrorDescription());

	}

	@Test
	void when_not_found_account_online_payment_validation_log_is_sent() {

		// given
		PaymentDTO paymentDTO = aPaymentDTO();

		when(paymentValidator.validate(paymentDTO)).thenReturn(new ResponseEntity<>("Successful", HttpStatus.OK));
		when(accountsRepository.findById(123)).thenReturn(null);

		// when
		paymentProcessor.processOnlinePayment(paymentDTO);

		// then
		verify(paymentLogsClient, times(1)).logError(paymentErrorDTOArgumentCaptor.capture());
		assertEquals("a_payment_id", paymentErrorDTOArgumentCaptor.getValue().getPaymentId());
		assertEquals("database", paymentErrorDTOArgumentCaptor.getValue().getErrorType());
		assertEquals("Account is not valid", paymentErrorDTOArgumentCaptor.getValue().getErrorDescription());

	}

	private Account anAccount() {
		return Account.builder()
				.accountId(123)
				.lastPaymentDate("01/09/22")
				.createdAt(Timestamp.valueOf(LocalDateTime.now().minusDays(10)))
				.email("hello@you.com")
				.build();
	}

	private PaymentDTO aPaymentDTO() {
		return PaymentDTO.builder()
				.paymentId("a_payment_id")
				.accountId("123")
				.paymentType(PaymentType.ONLINE.label)
				.amount(45)
				.creditCard("1111-2222")
				.build();
	}

}