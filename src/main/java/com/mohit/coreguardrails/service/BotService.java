package com.mohit.coreguardrails.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohit.coreguardrails.dto.BotRequestDTO;
import com.mohit.coreguardrails.dto.BotResponseDTO;
import com.mohit.coreguardrails.entity.Bot;
import com.mohit.coreguardrails.exception.AlreadyExistsException;
import com.mohit.coreguardrails.exception.NotFoundException;
import com.mohit.coreguardrails.repository.BotRepository;

import jakarta.transaction.Transactional;

@Service
public class BotService {

    @Autowired
    private BotRepository botRepository;

    @Transactional
    public BotResponseDTO createBot(BotRequestDTO dto) {

        botRepository.findByName(dto.getName())
                .ifPresent(bot -> {
                    throw new AlreadyExistsException(
                            "Bot already exists with name: " + dto.getName()
                    );
                });

        Bot bot = new Bot();
        bot.setName(dto.getName());
        bot.setPersonaDescription(dto.getPersonaDescription());

        Bot saved = botRepository.save(bot);

        return mapToDTO(saved);
    }

    public BotResponseDTO getBotByName(String name) {

        Bot bot = botRepository.findByName(name)
                .orElseThrow(() ->
                        new NotFoundException("Bot not found: " + name));

        return mapToDTO(bot);
    }

    public BotResponseDTO getBotById(Long id) {

        Bot bot = botRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Bot not found: " + id));

        return mapToDTO(bot);
    }

    public List<BotResponseDTO> getAllBots() {

        return botRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private BotResponseDTO mapToDTO(Bot bot) {
        BotResponseDTO dto = new BotResponseDTO();
        dto.setId(bot.getId());
        dto.setName(bot.getName());
        dto.setPersonaDescription(bot.getPersonaDescription());
        return dto;
    }
}