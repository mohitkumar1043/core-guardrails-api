package com.mohit.coreguardrails.dto;

import java.time.LocalDateTime;

import com.mohit.coreguardrails.entity.AuthorType;

import lombok.Data;

@Data
public class PostResponseDTO {

    private Long id;
    private Long authorId;
    private AuthorType authorType;
    private String content;
    private LocalDateTime createdAt;
}
