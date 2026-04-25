package com.mohit.coreguardrails.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mohit.coreguardrails.entity.AuthorType;
import com.mohit.coreguardrails.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	 List<Post> findByAuthorIdAndAuthorType(Long authorId, AuthorType authorType);
}
