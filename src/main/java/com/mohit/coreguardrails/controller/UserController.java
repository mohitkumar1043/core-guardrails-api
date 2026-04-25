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

import com.mohit.coreguardrails.dto.UserRequestDTO;
import com.mohit.coreguardrails.dto.UserResponseDTO;
import com.mohit.coreguardrails.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {
	 @Autowired
	    private UserService userService;
	 
	 @PostMapping
	    public ResponseEntity<UserResponseDTO> createUser(
	    		 @Valid   @RequestBody UserRequestDTO dto) {

	        UserResponseDTO response = userService.createUser(dto);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(response);
	    }
	 @GetMapping("/{id}")
	    public ResponseEntity<UserResponseDTO> getUserById(
	            @PathVariable Long id) {

	        UserResponseDTO response = userService.getUserById(id);

	        return ResponseEntity
	                .ok(response);
	    }

	 @GetMapping("/username/{username}")
	    public ResponseEntity<UserResponseDTO> getUserByUsername(
	            @PathVariable String username) {

	        UserResponseDTO response = userService.getUserByUsername(username);

	        return ResponseEntity
	                .ok(response);
	    }

	    
	 @GetMapping
	    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

	        List<UserResponseDTO> users = userService.getAllUsers();

	        return ResponseEntity
	                .ok(users);
	    }
}
