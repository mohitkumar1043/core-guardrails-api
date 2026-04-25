package com.mohit.coreguardrails.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mohit.coreguardrails.entity.AuthorType;
import com.mohit.coreguardrails.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
    List<Comment> findByPostId(Long postId);

    List<Comment> findByAuthorIdAndAuthorType(Long authorId, AuthorType authorType);

    List<Comment> findByPostIdAndParentCommentIsNull(Long postId);

    List<Comment> findByParentCommentId(Long parentCommentId);

    Long countByPostId(Long postId);

}
