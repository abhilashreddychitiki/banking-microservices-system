package com.banking.accountservice.dto;

import com.banking.accountservice.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    private Long id;
    private String accountNumber;
    private Long userId;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
