package com.mohit.coreguardrails.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mohit.coreguardrails.entity.Bot;

public interface BotRepository extends JpaRepository<Bot, Long>{
	Optional<Bot> findByName(String name);
}
