package com.banking.accountservice.service;

import com.banking.accountservice.dto.AccountRequestDto;
import com.banking.accountservice.dto.AccountResponseDto;
import com.banking.accountservice.dto.BalanceUpdateDto;
import com.banking.accountservice.model.AccountType;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    AccountResponseDto createAccount(AccountRequestDto accountRequestDto);
    
    AccountResponseDto getAccountById(Long id);
    
    AccountResponseDto getAccountByAccountNumber(String accountNumber);
    
    List<AccountResponseDto> getAccountsByUserId(Long userId);
    
    List<AccountResponseDto> getAccountsByUserIdAndType(Long userId, AccountType accountType);
    
    AccountResponseDto deposit(BalanceUpdateDto balanceUpdateDto);
    
    AccountResponseDto withdraw(BalanceUpdateDto balanceUpdateDto);
    
    void deleteAccount(Long id);
    
    AccountResponseDto updateAccountStatus(Long id, boolean active);
    
    BigDecimal getBalance(String accountNumber);
}
