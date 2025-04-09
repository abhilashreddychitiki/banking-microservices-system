package com.banking.transactionservice.client;

import com.banking.transactionservice.dto.AccountDto;
import com.banking.transactionservice.dto.BalanceUpdateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(name = "account-service")
public interface AccountServiceClient {
    
    @GetMapping("/api/accounts/number/{accountNumber}")
    ResponseEntity<AccountDto> getAccountByAccountNumber(@PathVariable String accountNumber);
    
    @PostMapping("/api/accounts/deposit")
    ResponseEntity<AccountDto> deposit(@RequestBody BalanceUpdateDto balanceUpdateDto);
    
    @PostMapping("/api/accounts/withdraw")
    ResponseEntity<AccountDto> withdraw(@RequestBody BalanceUpdateDto balanceUpdateDto);
    
    @GetMapping("/api/accounts/balance/{accountNumber}")
    ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber);
}
