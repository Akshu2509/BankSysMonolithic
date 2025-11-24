package com.example.demo.model.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotBlank(message = "Holder name cannot be empty")
    private String holderName;
}
