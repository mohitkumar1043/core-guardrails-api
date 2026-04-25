package com.mohit.coreguardrails.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mohit.coreguardrails.dto.UserRequestDTO;
import com.mohit.coreguardrails.dto.UserResponseDTO;
import com.mohit.coreguardrails.entity.User;
import com.mohit.coreguardrails.exception.AlreadyExistsException;
import com.mohit.coreguardrails.exception.NotFoundException;
import com.mohit.coreguardrails.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO dto) {

        
        userRepository.findByUsername(dto.getUsername())
                .ifPresent(user -> {
                    throw new AlreadyExistsException(
                            "User already exists with username: " + dto.getUsername()
                    );
                });

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPremium(dto.isPremium());

        User saved = userRepository.save(user);

        return mapToDTO(saved);
    }

    public UserResponseDTO getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundException("User not found: " + username));

        return mapToDTO(user);
    }

    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("User not found: " + id));

        return mapToDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPremium(user.isPremium());
        return dto;
    }
}