package com.banking.transactionservice.service;

import com.banking.transactionservice.client.AccountServiceClient;
import com.banking.transactionservice.dto.AccountDto;
import com.banking.transactionservice.dto.BalanceUpdateDto;
import com.banking.transactionservice.dto.TransactionRequestDto;
import com.banking.transactionservice.dto.TransactionResponseDto;
import com.banking.transactionservice.exception.InsufficientBalanceException;
import com.banking.transactionservice.exception.TransactionNotFoundException;
import com.banking.transactionservice.model.Transaction;
import com.banking.transactionservice.model.TransactionStatus;
import com.banking.transactionservice.model.TransactionType;
import com.banking.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Override
    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto transactionRequestDto) {
        // Validate the transaction
        validateTransaction(transactionRequestDto);

        // Get source account
        ResponseEntity<AccountDto> sourceAccountResponse = accountServiceClient.getAccountByAccountNumber(transactionRequestDto.getSourceAccountNumber());
        AccountDto sourceAccount = sourceAccountResponse.getBody();

        // Get destination account if it's a transfer
        AccountDto destinationAccount = null;
        if (transactionRequestDto.getType() == TransactionType.TRANSFER && transactionRequestDto.getDestinationAccountNumber() != null) {
            ResponseEntity<AccountDto> destinationAccountResponse = accountServiceClient.getAccountByAccountNumber(transactionRequestDto.getDestinationAccountNumber());
            destinationAccount = destinationAccountResponse.getBody();
        }

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setSourceAccountId(sourceAccount.getId());
        if (destinationAccount != null) {
            transaction.setDestinationAccountId(destinationAccount.getId());
        }
        transaction.setAmount(transactionRequestDto.getAmount());
        transaction.setType(transactionRequestDto.getType());
        transaction.setDescription(transactionRequestDto.getDescription());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setReference(transactionRequestDto.getReference() != null ? 
                transactionRequestDto.getReference() : generateReference());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Process the transaction
        try {
            processTransaction(savedTransaction, transactionRequestDto);
            savedTransaction.setStatus(TransactionStatus.COMPLETED);
        } catch (Exception e) {
            savedTransaction.setStatus(TransactionStatus.FAILED);
            throw e;
        } finally {
            transactionRepository.save(savedTransaction);
        }

        return mapToDto(savedTransaction);
    }

    private void validateTransaction(TransactionRequestDto transactionRequestDto) {
        // Check if source account exists
        ResponseEntity<AccountDto> sourceAccountResponse = accountServiceClient.getAccountByAccountNumber(transactionRequestDto.getSourceAccountNumber());
        if (sourceAccountResponse.getStatusCode().isError() || sourceAccountResponse.getBody() == null) {
            throw new IllegalArgumentException("Source account not found");
        }

        // Check if source account is active
        AccountDto sourceAccount = sourceAccountResponse.getBody();
        if (!sourceAccount.isActive()) {
            throw new IllegalArgumentException("Source account is not active");
        }

        // Check if destination account exists for transfers
        if (transactionRequestDto.getType() == TransactionType.TRANSFER && transactionRequestDto.getDestinationAccountNumber() != null) {
            ResponseEntity<AccountDto> destinationAccountResponse = accountServiceClient.getAccountByAccountNumber(transactionRequestDto.getDestinationAccountNumber());
            if (destinationAccountResponse.getStatusCode().isError() || destinationAccountResponse.getBody() == null) {
                throw new IllegalArgumentException("Destination account not found");
            }

            // Check if destination account is active
            AccountDto destinationAccount = destinationAccountResponse.getBody();
            if (!destinationAccount.isActive()) {
                throw new IllegalArgumentException("Destination account is not active");
            }
        }

        // Check if amount is positive
        if (transactionRequestDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }

        // Check if source account has sufficient balance for withdrawals and transfers
        if (transactionRequestDto.getType() == TransactionType.WITHDRAWAL || transactionRequestDto.getType() == TransactionType.TRANSFER) {
            ResponseEntity<BigDecimal> balanceResponse = accountServiceClient.getBalance(transactionRequestDto.getSourceAccountNumber());
            BigDecimal balance = balanceResponse.getBody();
            if (balance.compareTo(transactionRequestDto.getAmount()) < 0) {
                throw new InsufficientBalanceException("Insufficient balance for transaction");
            }
        }
    }

    private void processTransaction(Transaction transaction, TransactionRequestDto transactionRequestDto) {
        switch (transactionRequestDto.getType()) {
            case DEPOSIT:
                BalanceUpdateDto depositDto = new BalanceUpdateDto(
                        transactionRequestDto.getSourceAccountNumber(),
                        transactionRequestDto.getAmount(),
                        transactionRequestDto.getDescription()
                );
                accountServiceClient.deposit(depositDto);
                break;
            case WITHDRAWAL:
                BalanceUpdateDto withdrawalDto = new BalanceUpdateDto(
                        transactionRequestDto.getSourceAccountNumber(),
                        transactionRequestDto.getAmount(),
                        transactionRequestDto.getDescription()
                );
                accountServiceClient.withdraw(withdrawalDto);
                break;
            case TRANSFER:
                // Withdraw from source account
                BalanceUpdateDto sourceWithdrawalDto = new BalanceUpdateDto(
                        transactionRequestDto.getSourceAccountNumber(),
                        transactionRequestDto.getAmount(),
                        "Transfer to " + transactionRequestDto.getDestinationAccountNumber()
                );
                accountServiceClient.withdraw(sourceWithdrawalDto);

                // Deposit to destination account
                BalanceUpdateDto destinationDepositDto = new BalanceUpdateDto(
                        transactionRequestDto.getDestinationAccountNumber(),
                        transactionRequestDto.getAmount(),
                        "Transfer from " + transactionRequestDto.getSourceAccountNumber()
                );
                accountServiceClient.deposit(destinationDepositDto);
                break;
            default:
                throw new IllegalArgumentException("Unsupported transaction type");
        }
    }

    private String generateReference() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }

    @Override
    public TransactionResponseDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        return mapToDto(transaction);
    }

    @Override
    public TransactionResponseDto getTransactionByReference(String reference) {
        Transaction transaction = transactionRepository.findByReference(reference)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with reference: " + reference));
        return mapToDto(transaction);
    }

    @Override
    public List<TransactionResponseDto> getTransactionsBySourceAccountNumber(String sourceAccountNumber) {
        ResponseEntity<AccountDto> accountResponse = accountServiceClient.getAccountByAccountNumber(sourceAccountNumber);
        AccountDto account = accountResponse.getBody();
        List<Transaction> transactions = transactionRepository.findBySourceAccountId(account.getId());
        return transactions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByDestinationAccountNumber(String destinationAccountNumber) {
        ResponseEntity<AccountDto> accountResponse = accountServiceClient.getAccountByAccountNumber(destinationAccountNumber);
        AccountDto account = accountResponse.getBody();
        List<Transaction> transactions = transactionRepository.findByDestinationAccountId(account.getId());
        return transactions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByAccountNumber(String accountNumber) {
        ResponseEntity<AccountDto> accountResponse = accountServiceClient.getAccountByAccountNumber(accountNumber);
        AccountDto account = accountResponse.getBody();
        List<Transaction> transactions = transactionRepository.findBySourceAccountIdOrDestinationAccountId(
                account.getId(), account.getId());
        return transactions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByType(TransactionType type) {
        List<Transaction> transactions = transactionRepository.findByType(type);
        return transactions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByStatus(TransactionStatus status) {
        List<Transaction> transactions = transactionRepository.findByStatus(status);
        return transactions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponseDto> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByCreatedAtBetween(startDate, endDate);
        return transactions.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsBySourceAccountNumber(String sourceAccountNumber, Pageable pageable) {
        ResponseEntity<AccountDto> accountResponse = accountServiceClient.getAccountByAccountNumber(sourceAccountNumber);
        AccountDto account = accountResponse.getBody();
        Page<Transaction> transactions = transactionRepository.findBySourceAccountId(account.getId(), pageable);
        return transactions.map(this::mapToDto);
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByDestinationAccountNumber(String destinationAccountNumber, Pageable pageable) {
        ResponseEntity<AccountDto> accountResponse = accountServiceClient.getAccountByAccountNumber(destinationAccountNumber);
        AccountDto account = accountResponse.getBody();
        Page<Transaction> transactions = transactionRepository.findByDestinationAccountId(account.getId(), pageable);
        return transactions.map(this::mapToDto);
    }

    @Override
    public Page<TransactionResponseDto> getTransactionsByAccountNumber(String accountNumber, Pageable pageable) {
        ResponseEntity<AccountDto> accountResponse = accountServiceClient.getAccountByAccountNumber(accountNumber);
        AccountDto account = accountResponse.getBody();
        Page<Transaction> transactions = transactionRepository.findBySourceAccountIdOrDestinationAccountId(
                account.getId(), account.getId(), pageable);
        return transactions.map(this::mapToDto);
    }

    @Override
    @Transactional
    public TransactionResponseDto updateTransactionStatus(Long id, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        transaction.setStatus(status);
        transaction.setUpdatedAt(LocalDateTime.now());
        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToDto(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with id: " + id));
        transactionRepository.delete(transaction);
    }

    private TransactionResponseDto mapToDto(Transaction transaction) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setId(transaction.getId());
        
        // Get source account number
        if (transaction.getSourceAccountId() != null) {
            try {
                ResponseEntity<AccountDto> sourceAccountResponse = accountServiceClient.getAccountByAccountNumber(
                        getAccountNumberById(transaction.getSourceAccountId()));
                if (sourceAccountResponse.getStatusCode().is2xxSuccessful() && sourceAccountResponse.getBody() != null) {
                    dto.setSourceAccountNumber(sourceAccountResponse.getBody().getAccountNumber());
                }
            } catch (Exception e) {
                // Handle case where account service is unavailable
                dto.setSourceAccountNumber("Account ID: " + transaction.getSourceAccountId());
            }
        }
        
        // Get destination account number
        if (transaction.getDestinationAccountId() != null) {
            try {
                ResponseEntity<AccountDto> destinationAccountResponse = accountServiceClient.getAccountByAccountNumber(
                        getAccountNumberById(transaction.getDestinationAccountId()));
                if (destinationAccountResponse.getStatusCode().is2xxSuccessful() && destinationAccountResponse.getBody() != null) {
                    dto.setDestinationAccountNumber(destinationAccountResponse.getBody().getAccountNumber());
                }
            } catch (Exception e) {
                // Handle case where account service is unavailable
                dto.setDestinationAccountNumber("Account ID: " + transaction.getDestinationAccountId());
            }
        }
        
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setDescription(transaction.getDescription());
        dto.setStatus(transaction.getStatus());
        dto.setReference(transaction.getReference());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        return dto;
    }
    
    // This is a placeholder method - in a real implementation, you would have a way to get account number by ID
    private String getAccountNumberById(Long accountId) {
        // In a real implementation, you would call the account service to get the account number
        // For now, we'll just return a placeholder
        return "ACC" + accountId;
    }
}
