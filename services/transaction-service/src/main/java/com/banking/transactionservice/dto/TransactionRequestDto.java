package com.banking.transactionservice.dto;

import com.banking.transactionservice.model.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {
    @NotBlank
    private String sourceAccountNumber;
    
    private String destinationAccountNumber;
    
    @NotNull
    @Positive
    private BigDecimal amount;
    
    @NotNull
    private TransactionType type;
    
    private String description;
    
    private String reference;
}
