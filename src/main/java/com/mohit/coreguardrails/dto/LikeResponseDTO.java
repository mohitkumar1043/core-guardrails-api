package com.mohit.coreguardrails.dto;

import lombok.Data;

@Data
public class LikeResponseDTO {

    private Long postId;
    private Long userId;
    private String message;
}
