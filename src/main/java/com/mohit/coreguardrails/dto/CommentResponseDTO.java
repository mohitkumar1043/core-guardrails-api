package com.mohit.coreguardrails.dto;

import java.time.LocalDateTime;

import com.mohit.coreguardrails.entity.AuthorType;

import lombok.Data;

@Data
public class CommentResponseDTO {

    private Long id;

    private Long postId;

    private Long authorId;
    private AuthorType authorType;

    private String content;

    private int depthLevel;

    private Long parentCommentId;

    private LocalDateTime createdAt;
}
