package com.banking.demo.repository;

import com.banking.demo.model.Account;
import com.banking.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount(Account account);
    Optional<Transaction> findByTransactionId(String transactionId);
}
