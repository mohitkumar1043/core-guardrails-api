package com.mohit.coreguardrails.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.mohit.coreguardrails.dto.CommentRequestDTO;
import com.mohit.coreguardrails.dto.CommentResponseDTO;
import com.mohit.coreguardrails.entity.AuthorType;
import com.mohit.coreguardrails.entity.Comment;
import com.mohit.coreguardrails.entity.Post;
import com.mohit.coreguardrails.exception.BadRequestException;
import com.mohit.coreguardrails.exception.NotFoundException;
import com.mohit.coreguardrails.exception.TooManyRequestsException;
import com.mohit.coreguardrails.repository.BotRepository;
import com.mohit.coreguardrails.repository.CommentRepository;
import com.mohit.coreguardrails.repository.PostRepository;
import com.mohit.coreguardrails.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BotRepository botRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private NotificationService notificationService;

    private static final int MAX_DEPTH = 20;
    private static final int MAX_BOT_REPLIES = 100;

    @Transactional
    public CommentResponseDTO addComment(Long postId, CommentRequestDTO dto) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found: " + postId));

        validateAuthor(dto.getAuthorId(), dto.getAuthorType());

        Comment parent = null;
        int depth = 1;

        if (dto.getParentCommentId() != null) {

            parent = commentRepository.findById(dto.getParentCommentId())
                    .orElseThrow(() -> new NotFoundException("Parent comment not found"));

            depth = parent.getDepthLevel() + 1;

            
            if (depth > MAX_DEPTH) {
                throw new BadRequestException("Max comment depth is 20");
            }
        }

       
        if (dto.getAuthorType() == AuthorType.BOT) {
        	notificationService.handleBotNotification(
        	        parent.getAuthorId(), 
        	        dto.getAuthorId()       
        	);
            
            String botCountKey = "post:" + postId + ":bot_count";

            Long count = redisTemplate.opsForValue().increment(botCountKey);

            if (count != null && count > MAX_BOT_REPLIES) {
                redisTemplate.opsForValue().decrement(botCountKey);
                throw new TooManyRequestsException("Bot limit exceeded (100)");
            }

            if (parent != null && parent.getAuthorType() == AuthorType.USER) {

                String cooldownKey = "cooldown:bot_" + dto.getAuthorId()
                        + ":human_" + parent.getAuthorId();

                if (Boolean.TRUE.equals(redisTemplate.hasKey(cooldownKey))) {
                    throw new TooManyRequestsException("Cooldown active (10 min)");
                }

                redisTemplate.opsForValue()
                        .set(cooldownKey, "1", 10, TimeUnit.MINUTES);
            }
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthorId(dto.getAuthorId());
        comment.setAuthorType(dto.getAuthorType());
        comment.setContent(dto.getContent());
        comment.setParentComment(parent);
        comment.setDepthLevel(depth);

        Comment saved = commentRepository.save(comment);

        updateVirality(postId, dto.getAuthorType());

        return mapToDTO(saved);
    }


    private void validateAuthor(Long authorId, AuthorType type) {

        if (type == AuthorType.USER) {
            userRepository.findById(authorId)
                    .orElseThrow(() -> new NotFoundException("User not found"));
        } else {
            botRepository.findById(authorId)
                    .orElseThrow(() -> new NotFoundException("Bot not found"));
        }
    }


    private void updateVirality(Long postId, AuthorType type) {

        String key = "post:" + postId + ":virality";

        if (type == AuthorType.USER) {
            redisTemplate.opsForValue().increment(key, 50);
        } else {
            redisTemplate.opsForValue().increment(key, 1);
        }
    }
    
    public List<CommentResponseDTO> getCommentsByPost(Long postId) {

        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found: " + postId));

       
        List<Comment> comments = commentRepository.findByPostId(postId);

        
        return comments.stream()
                .map(this::mapToDTO)
                .toList();
    }
    private CommentResponseDTO mapToDTO(Comment comment) {

        CommentResponseDTO dto = new CommentResponseDTO();

        dto.setId(comment.getId());
        dto.setPostId(comment.getPost().getId());
        dto.setAuthorId(comment.getAuthorId());
        dto.setAuthorType(comment.getAuthorType());
        dto.setContent(comment.getContent());
        dto.setDepthLevel(comment.getDepthLevel());
        dto.setCreatedAt(comment.getCreatedAt());

        if (comment.getParentComment() != null) {
            dto.setParentCommentId(comment.getParentComment().getId());
        }

        return dto;
    }
}