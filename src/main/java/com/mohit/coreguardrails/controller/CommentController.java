package com.mohit.coreguardrails.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mohit.coreguardrails.dto.CommentRequestDTO;
import com.mohit.coreguardrails.dto.CommentResponseDTO;
import com.mohit.coreguardrails.service.CommentService;

@RestController
@RequestMapping("/api/posts")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDTO dto) {

        CommentResponseDTO response = commentService.addComment(postId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getComments(
            @PathVariable Long postId) {

        List<CommentResponseDTO> response =
                commentService.getCommentsByPost(postId);

        return ResponseEntity.ok(response);
    }
}
