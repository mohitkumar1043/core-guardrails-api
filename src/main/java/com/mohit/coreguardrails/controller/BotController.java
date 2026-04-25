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

import com.mohit.coreguardrails.dto.BotRequestDTO;
import com.mohit.coreguardrails.dto.BotResponseDTO;
import com.mohit.coreguardrails.service.BotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bots")
public class BotController {

    @Autowired
    private BotService botService;

    @PostMapping
    public ResponseEntity<BotResponseDTO> createBot(
            @Valid @RequestBody BotRequestDTO dto) {

        BotResponseDTO response = botService.createBot(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BotResponseDTO> getBotById(
            @PathVariable Long id) {

        BotResponseDTO response = botService.getBotById(id);

        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BotResponseDTO> getBotByName(
            @PathVariable String name) {

        BotResponseDTO response = botService.getBotByName(name);

        return ResponseEntity
                .ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BotResponseDTO>> getAllBots() {

        List<BotResponseDTO> bots = botService.getAllBots();

        return ResponseEntity
                .ok(bots);
    }
}

