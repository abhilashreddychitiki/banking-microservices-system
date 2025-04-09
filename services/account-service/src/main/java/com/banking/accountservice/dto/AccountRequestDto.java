package com.banking.accountservice.dto;

import com.banking.accountservice.model.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {
    @NotNull
    private Long userId;
    
    @NotNull
    private AccountType accountType;
    
    @Positive
    private BigDecimal initialDeposit;
    
    private String currency = "USD"; // Default currency
}
