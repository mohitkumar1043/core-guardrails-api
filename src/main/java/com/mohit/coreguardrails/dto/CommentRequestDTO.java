package com.mohit.coreguardrails.dto;

import com.mohit.coreguardrails.entity.AuthorType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDTO {

    @NotNull(message = "Author ID is required")
    private Long authorId;

    @NotNull(message = "Author type is required")
    private AuthorType authorType; 

    @NotBlank(message = "Content cannot be empty")
    @Size(max = 500, message = "Content can be up to 2000 characters")
    private String content;

    // null for root comment
    private Long parentCommentId;
}