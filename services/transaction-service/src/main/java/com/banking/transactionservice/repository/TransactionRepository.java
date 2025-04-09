package com.banking.transactionservice.repository;

import com.banking.transactionservice.model.Transaction;
import com.banking.transactionservice.model.TransactionStatus;
import com.banking.transactionservice.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountId(Long sourceAccountId);
    
    List<Transaction> findByDestinationAccountId(Long destinationAccountId);
    
    List<Transaction> findBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId);
    
    List<Transaction> findByType(TransactionType type);
    
    List<Transaction> findByStatus(TransactionStatus status);
    
    List<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    Optional<Transaction> findByReference(String reference);
    
    Page<Transaction> findBySourceAccountId(Long sourceAccountId, Pageable pageable);
    
    Page<Transaction> findByDestinationAccountId(Long destinationAccountId, Pageable pageable);
    
    Page<Transaction> findBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId, Pageable pageable);
}
