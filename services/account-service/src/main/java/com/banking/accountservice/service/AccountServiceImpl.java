package com.banking.accountservice.service;

import com.banking.accountservice.dto.AccountRequestDto;
import com.banking.accountservice.dto.AccountResponseDto;
import com.banking.accountservice.dto.BalanceUpdateDto;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.exception.InsufficientBalanceException;
import com.banking.accountservice.model.Account;
import com.banking.accountservice.model.AccountType;
import com.banking.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountResponseDto createAccount(AccountRequestDto accountRequestDto) {
        Account account = new Account();
        account.setUserId(accountRequestDto.getUserId());
        account.setAccountType(accountRequestDto.getAccountType());
        account.setBalance(accountRequestDto.getInitialDeposit() != null ? accountRequestDto.getInitialDeposit() : BigDecimal.ZERO);
        account.setCurrency(accountRequestDto.getCurrency());
        account.setActive(true);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        
        // Generate a unique account number
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        
        account.setAccountNumber(accountNumber);
        
        Account savedAccount = accountRepository.save(account);
        
        // TODO: Publish account created event to notification service
        
        return mapToDto(savedAccount);
    }

    @Override
    public AccountResponseDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        return mapToDto(account);
    }

    @Override
    public AccountResponseDto getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + accountNumber));
        return mapToDto(account);
    }

    @Override
    public List<AccountResponseDto> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountResponseDto> getAccountsByUserIdAndType(Long userId, AccountType accountType) {
        List<Account> accounts = accountRepository.findByUserIdAndAccountType(userId, accountType);
        return accounts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponseDto deposit(BalanceUpdateDto balanceUpdateDto) {
        Account account = accountRepository.findByAccountNumber(balanceUpdateDto.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + balanceUpdateDto.getAccountNumber()));
        
        if (!account.isActive()) {
            throw new IllegalStateException("Account is not active");
        }
        
        account.setBalance(account.getBalance().add(balanceUpdateDto.getAmount()));
        account.setUpdatedAt(LocalDateTime.now());
        
        Account updatedAccount = accountRepository.save(account);
        
        // TODO: Publish transaction event to transaction service
        
        return mapToDto(updatedAccount);
    }

    @Override
    @Transactional
    public AccountResponseDto withdraw(BalanceUpdateDto balanceUpdateDto) {
        Account account = accountRepository.findByAccountNumber(balanceUpdateDto.getAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + balanceUpdateDto.getAccountNumber()));
        
        if (!account.isActive()) {
            throw new IllegalStateException("Account is not active");
        }
        
        if (account.getBalance().compareTo(balanceUpdateDto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        
        account.setBalance(account.getBalance().subtract(balanceUpdateDto.getAmount()));
        account.setUpdatedAt(LocalDateTime.now());
        
        Account updatedAccount = accountRepository.save(account);
        
        // TODO: Publish transaction event to transaction service
        
        return mapToDto(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        
        // Instead of deleting, we deactivate the account
        account.setActive(false);
        account.setUpdatedAt(LocalDateTime.now());
        
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public AccountResponseDto updateAccountStatus(Long id, boolean active) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        
        account.setActive(active);
        account.setUpdatedAt(LocalDateTime.now());
        
        Account updatedAccount = accountRepository.save(account);
        
        return mapToDto(updatedAccount);
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with account number: " + accountNumber));
        
        return account.getBalance();
    }
    
    private String generateAccountNumber() {
        // Generate a random 10-digit account number
        return String.format("%010d", Math.abs(UUID.randomUUID().getLeastSignificantBits() % 10000000000L));
    }
    
    private AccountResponseDto mapToDto(Account account) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setUserId(account.getUserId());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        dto.setActive(account.isActive());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }
}
