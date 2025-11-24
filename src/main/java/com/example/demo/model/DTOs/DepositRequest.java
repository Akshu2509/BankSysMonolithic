package com.example.demo.model.DTOs;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class DepositRequest {

    @Min(value = 1, message = "Amount must be greater than zero")
    private double amount;
}
