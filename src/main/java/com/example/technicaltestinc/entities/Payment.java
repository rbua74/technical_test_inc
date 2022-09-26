package com.example.technicaltestinc.entities;

import com.example.technicaltestinc.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

	@Id
	@Column(name = "payment_id", unique = true, nullable = false)
	private String paymentId;

	@OneToOne
	@JoinColumn(name = "accountId")
	private Account account;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_type")
	private PaymentType paymentType;

	@Column(name = "credit_card")
	private String creditCard;

	@Column(name = "amount", nullable = false)
	private int amount;

	@Column(name = "created_at", nullable = false)
	private Timestamp createdAt;

}
