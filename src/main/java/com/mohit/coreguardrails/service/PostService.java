package com.mohit.coreguardrails.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohit.coreguardrails.dto.PostRequestDTO;
import com.mohit.coreguardrails.dto.PostResponseDTO;
import com.mohit.coreguardrails.entity.AuthorType;
import com.mohit.coreguardrails.entity.Post;
import com.mohit.coreguardrails.exception.NotFoundException;
import com.mohit.coreguardrails.repository.BotRepository;
import com.mohit.coreguardrails.repository.PostRepository;
import com.mohit.coreguardrails.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BotRepository botRepository;

    @Transactional
    public PostResponseDTO createPost(PostRequestDTO dto) {

       
        if (dto.getAuthorType() == AuthorType.USER) {

            userRepository.findById(dto.getAuthorId())
                    .orElseThrow(() ->
                            new NotFoundException("User not found: " + dto.getAuthorId()));

        } else {

            botRepository.findById(dto.getAuthorId())
                    .orElseThrow(() ->
                            new NotFoundException("Bot not found: " + dto.getAuthorId()));
        }

        Post post = new Post();
        post.setAuthorId(dto.getAuthorId());
        post.setAuthorType(dto.getAuthorType());
        post.setContent(dto.getContent());

        Post saved = postRepository.save(post);

        return mapToDTO(saved);
    }

    public PostResponseDTO getPostById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Post not found: " + id));

        return mapToDTO(post);
    }

    public List<PostResponseDTO> getAllPosts() {

        return postRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PostResponseDTO> getPostsByAuthorName(String name) {

        List<Post> posts = new ArrayList<>();

        userRepository.findByUsername(name).ifPresent(user -> {
            posts.addAll(
                    postRepository.findByAuthorIdAndAuthorType(
                            user.getId(), AuthorType.USER)
            );
        });

        botRepository.findByName(name).ifPresent(bot -> {
            posts.addAll(
                    postRepository.findByAuthorIdAndAuthorType(
                            bot.getId(), AuthorType.BOT)
            );
        });

        if (posts.isEmpty()) {
            throw new NotFoundException("No posts found for author: " + name);
        }

        return posts.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PostResponseDTO mapToDTO(Post post) {

        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setAuthorId(post.getAuthorId());
        dto.setAuthorType(post.getAuthorType());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());

        return dto;
    }
}
