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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mohit.coreguardrails.dto.PostRequestDTO;
import com.mohit.coreguardrails.dto.PostResponseDTO;
import com.mohit.coreguardrails.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @Valid @RequestBody PostRequestDTO dto) {

        PostResponseDTO response = postService.createPost(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(
            @PathVariable Long id) {

        PostResponseDTO response = postService.getPostById(id);

        return ResponseEntity
                .ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {

        List<PostResponseDTO> posts = postService.getAllPosts();

        return ResponseEntity
                .ok(posts);
    }

    
    @GetMapping("/author/by-name")
    public ResponseEntity<List<PostResponseDTO>> getPostsByAuthorName(
            @RequestParam String name) {

        List<PostResponseDTO> posts =
                postService.getPostsByAuthorName(name);

        return ResponseEntity.ok(posts);
    }
}
