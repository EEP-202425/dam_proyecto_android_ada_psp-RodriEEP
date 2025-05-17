package com.volantum.controller;

import com.volantum.domain.User;
import com.volantum.dto.LoginRequestDTO;
import com.volantum.dto.RegisterRequestDTO;
import com.volantum.dto.UserResponseDTO;
import com.volantum.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user
     * @param registerRequest the request to register a new user
     * @return the user registered
     */
    @PostMapping("/register")
    @Operation(summary = "Registers a new user", description = "Registers a new user with the given request")
    public ResponseEntity<UserResponseDTO> register(
		@Parameter(description = "The request to register a new user")
		@RequestBody RegisterRequestDTO registerRequest
	) {
        User user = new User(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(), registerRequest.getPassword());
        User savedUser = userService.register(user);
        return ResponseEntity.ok(userService.convertToDTO(savedUser));
    }

    /**
     * Logs in a user
     * @param loginRequest the request to log in a user
     * @return the user logged in
     */
    @PostMapping("/login")
    @Operation(summary = "Logs in a user", description = "Logs in a user with the given request")
    public ResponseEntity<UserResponseDTO> login(
		@Parameter(description = "The request to log in a user")
		@RequestBody LoginRequestDTO loginRequest
	) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(userService.convertToDTO(user));
    }

    /**
     * Gets a user by id
     * @param id the id of the user to get
     * @return the user found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Gets a user by id", description = "Gets a user by id")
        public ResponseEntity<UserResponseDTO> getUser(
		@Parameter(description = "The id of the user to get")
		@PathVariable int id
	) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(userService.convertToDTO(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}