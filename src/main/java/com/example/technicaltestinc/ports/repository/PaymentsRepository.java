package com.example.technicaltestinc.ports.repository;

import com.example.technicaltestinc.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PaymentsRepository extends JpaRepository<Payment, String> {
}
