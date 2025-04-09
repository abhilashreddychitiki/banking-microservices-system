package com.banking.accountservice.controller;

import com.banking.accountservice.dto.AccountRequestDto;
import com.banking.accountservice.dto.AccountResponseDto;
import com.banking.accountservice.dto.BalanceUpdateDto;
import com.banking.accountservice.model.AccountType;
import com.banking.accountservice.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto accountRequestDto) {
        AccountResponseDto createdAccount = accountService.createAccount(accountRequestDto);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long id) {
        AccountResponseDto account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByAccountNumber(@PathVariable String accountNumber) {
        AccountResponseDto account = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUserId(@PathVariable Long userId) {
        List<AccountResponseDto> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/user/{userId}/type/{accountType}")
    public ResponseEntity<List<AccountResponseDto>> getAccountsByUserIdAndType(
            @PathVariable Long userId,
            @PathVariable AccountType accountType) {
        List<AccountResponseDto> accounts = accountService.getAccountsByUserIdAndType(userId, accountType);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountResponseDto> deposit(@Valid @RequestBody BalanceUpdateDto balanceUpdateDto) {
        AccountResponseDto updatedAccount = accountService.deposit(balanceUpdateDto);
        return ResponseEntity.ok(updatedAccount);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountResponseDto> withdraw(@Valid @RequestBody BalanceUpdateDto balanceUpdateDto) {
        AccountResponseDto updatedAccount = accountService.withdraw(balanceUpdateDto);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountResponseDto> updateAccountStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        AccountResponseDto updatedAccount = accountService.updateAccountStatus(id, active);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        BigDecimal balance = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(balance);
    }
}
