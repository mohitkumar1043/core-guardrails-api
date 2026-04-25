package com.mohit.coreguardrails.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String username;
    private boolean isPremium;
}