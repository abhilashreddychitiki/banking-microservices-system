package com.banking.transactionservice.service;

import com.banking.transactionservice.dto.TransactionRequestDto;
import com.banking.transactionservice.dto.TransactionResponseDto;
import com.banking.transactionservice.model.TransactionStatus;
import com.banking.transactionservice.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionResponseDto createTransaction(TransactionRequestDto transactionRequestDto);
    
    TransactionResponseDto getTransactionById(Long id);
    
    TransactionResponseDto getTransactionByReference(String reference);
    
    List<TransactionResponseDto> getTransactionsBySourceAccountNumber(String sourceAccountNumber);
    
    List<TransactionResponseDto> getTransactionsByDestinationAccountNumber(String destinationAccountNumber);
    
    List<TransactionResponseDto> getTransactionsByAccountNumber(String accountNumber);
    
    List<TransactionResponseDto> getTransactionsByType(TransactionType type);
    
    List<TransactionResponseDto> getTransactionsByStatus(TransactionStatus status);
    
    List<TransactionResponseDto> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    Page<TransactionResponseDto> getTransactionsBySourceAccountNumber(String sourceAccountNumber, Pageable pageable);
    
    Page<TransactionResponseDto> getTransactionsByDestinationAccountNumber(String destinationAccountNumber, Pageable pageable);
    
    Page<TransactionResponseDto> getTransactionsByAccountNumber(String accountNumber, Pageable pageable);
    
    TransactionResponseDto updateTransactionStatus(Long id, TransactionStatus status);
    
    void deleteTransaction(Long id);
}
