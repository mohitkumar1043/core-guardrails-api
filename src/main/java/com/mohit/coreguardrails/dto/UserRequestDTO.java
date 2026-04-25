package com.mohit.coreguardrails.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "User name cannot be empty")
    @Size(min = 3, max = 100)
    private String username;

    private boolean isPremium;
}
