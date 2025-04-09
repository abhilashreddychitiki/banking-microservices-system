package com.banking.transactionservice.controller;

import com.banking.transactionservice.dto.TransactionRequestDto;
import com.banking.transactionservice.dto.TransactionResponseDto;
import com.banking.transactionservice.model.TransactionStatus;
import com.banking.transactionservice.model.TransactionType;
import com.banking.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto transactionRequestDto) {
        TransactionResponseDto createdTransaction = transactionService.createTransaction(transactionRequestDto);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable Long id) {
        TransactionResponseDto transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<TransactionResponseDto> getTransactionByReference(@PathVariable String reference) {
        TransactionResponseDto transaction = transactionService.getTransactionByReference(reference);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/source/{sourceAccountNumber}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsBySourceAccountNumber(
            @PathVariable String sourceAccountNumber) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsBySourceAccountNumber(sourceAccountNumber);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/destination/{destinationAccountNumber}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByDestinationAccountNumber(
            @PathVariable String destinationAccountNumber) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByDestinationAccountNumber(destinationAccountNumber);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByAccountNumber(
            @PathVariable String accountNumber) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByType(
            @PathVariable TransactionType type) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByStatus(
            @PathVariable TransactionStatus status) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByStatus(status);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/source/{sourceAccountNumber}/paged")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsBySourceAccountNumberPaged(
            @PathVariable String sourceAccountNumber,
            Pageable pageable) {
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsBySourceAccountNumber(sourceAccountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/destination/{destinationAccountNumber}/paged")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByDestinationAccountNumberPaged(
            @PathVariable String destinationAccountNumber,
            Pageable pageable) {
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByDestinationAccountNumber(destinationAccountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountNumber}/paged")
    public ResponseEntity<Page<TransactionResponseDto>> getTransactionsByAccountNumberPaged(
            @PathVariable String accountNumber,
            Pageable pageable) {
        Page<TransactionResponseDto> transactions = transactionService.getTransactionsByAccountNumber(accountNumber, pageable);
        return ResponseEntity.ok(transactions);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TransactionResponseDto> updateTransactionStatus(
            @PathVariable Long id,
            @RequestParam TransactionStatus status) {
        TransactionResponseDto updatedTransaction = transactionService.updateTransactionStatus(id, status);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
