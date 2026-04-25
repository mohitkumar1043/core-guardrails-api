package com.mohit.coreguardrails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.mohit.coreguardrails.dto.LikeRequestDTO;
import com.mohit.coreguardrails.dto.LikeResponseDTO;
import com.mohit.coreguardrails.entity.Post;
import com.mohit.coreguardrails.entity.PostLike;
import com.mohit.coreguardrails.entity.User;
import com.mohit.coreguardrails.exception.BadRequestException;
import com.mohit.coreguardrails.exception.NotFoundException;
import com.mohit.coreguardrails.repository.PostLikeRepository;
import com.mohit.coreguardrails.repository.PostRepository;
import com.mohit.coreguardrails.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Transactional
    public LikeResponseDTO likePost(Long postId, LikeRequestDTO dto) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        
        if (postLikeRepository.findByPostIdAndUserId(postId, dto.getUserId()).isPresent()) {
            throw new BadRequestException("User already liked this post");
        }

        
        PostLike like = new PostLike();
        like.setPost(post);
        like.setUser(user);

        postLikeRepository.save(like);

       
        redisTemplate.opsForValue()
                .increment("post:" + postId + ":virality", 20);

        
        LikeResponseDTO response = new LikeResponseDTO();
        response.setPostId(postId);
        response.setUserId(dto.getUserId());
        response.setMessage("Post liked successfully");

        return response;
    }

    @Transactional
    public LikeResponseDTO unlikePost(Long postId, Long userId) {

        PostLike like = postLikeRepository
                .findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new NotFoundException("Like not found"));

        postLikeRepository.delete(like);

        
        redisTemplate.opsForValue()
                .decrement("post:" + postId + ":virality", 20);

        LikeResponseDTO response = new LikeResponseDTO();
        response.setPostId(postId);
        response.setUserId(userId);
        response.setMessage("Post unliked successfully");

        return response;
    }
}