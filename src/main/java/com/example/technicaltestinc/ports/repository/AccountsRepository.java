package com.example.technicaltestinc.ports.repository;

import com.example.technicaltestinc.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountsRepository extends JpaRepository<Account, Integer> {

//	@Query("FROM Account where accountId = :accountId")
//	Account getAccountDetailsById(@Param("accountId") String email);
}
