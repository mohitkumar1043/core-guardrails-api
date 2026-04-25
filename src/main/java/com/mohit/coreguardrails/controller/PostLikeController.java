package com.mohit.coreguardrails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mohit.coreguardrails.dto.LikeRequestDTO;
import com.mohit.coreguardrails.dto.LikeResponseDTO;
import com.mohit.coreguardrails.service.PostLikeService;

@RestController
@RequestMapping("/api/posts")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    
    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeResponseDTO> likePost(
            @PathVariable Long postId,
            @RequestBody LikeRequestDTO dto) {

        LikeResponseDTO response = postLikeService.likePost(postId, dto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}/unlike")
    public ResponseEntity<LikeResponseDTO> unlikePost(
            @PathVariable Long postId,
            @RequestBody LikeRequestDTO dto) {

        LikeResponseDTO response =
                postLikeService.unlikePost(postId, dto.getUserId());

        return ResponseEntity.ok(response);
    }
}