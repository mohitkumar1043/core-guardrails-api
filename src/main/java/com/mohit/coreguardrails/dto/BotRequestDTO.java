package com.mohit.coreguardrails.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BotRequestDTO {

    @NotBlank(message = "Bot name cannot be empty")
    @Size(min = 3, max = 100, message = "Bot name must be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Persona description cannot be empty")
    @Size(max = 1000, message = "Persona description can be up to 1000 characters")
    private String personaDescription;
}