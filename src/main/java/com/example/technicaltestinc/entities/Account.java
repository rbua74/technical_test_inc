package com.example.technicaltestinc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "account_id", unique = true, nullable = false)
	private int accountId;

	@Column(name = "email", length = 65)
	private String email;

	@Column(name = "birthdate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime birthdate;

	@Column(name = "last_payment_date")
	private Timestamp lastPaymentDate;

	@Column(name = "created_on")
	private Timestamp createdAt;


}
