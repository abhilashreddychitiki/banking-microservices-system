package com.banking.demo.service;

import com.banking.demo.model.Account;
import com.banking.demo.model.Transaction;
import com.banking.demo.model.TransactionType;
import com.banking.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Optional<Transaction> getTransactionByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount, String description) {
        Account account = accountService.getAccountByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Update account balance
        account.setBalance(account.getBalance().add(amount));
        accountService.updateAccount(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setDestinationAccount(accountNumber);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount, String description) {
        Account account = accountService.getAccountByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Update account balance
        account.setBalance(account.getBalance().subtract(amount));
        accountService.updateAccount(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setSourceAccount(accountNumber);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccount(account);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transfer(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount, String description) {
        Account sourceAccount = accountService.getAccountByNumber(sourceAccountNumber)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        
        Account destinationAccount = accountService.getAccountByNumber(destinationAccountNumber)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Update account balances
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
        
        accountService.updateAccount(sourceAccount);
        accountService.updateAccount(destinationAccount);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setAmount(amount);
        transaction.setSourceAccount(sourceAccountNumber);
        transaction.setDestinationAccount(destinationAccountNumber);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAccount(sourceAccount);

        return transactionRepository.save(transaction);
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
