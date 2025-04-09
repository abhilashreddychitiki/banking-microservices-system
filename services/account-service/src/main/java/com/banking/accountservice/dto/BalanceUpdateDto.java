package com.banking.accountservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateDto {
    @NotBlank
    private String accountNumber;
    
    @Positive
    private BigDecimal amount;
    
    private String description;
}
