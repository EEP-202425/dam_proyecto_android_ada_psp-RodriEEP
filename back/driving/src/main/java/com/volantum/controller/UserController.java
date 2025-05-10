package com.volantum.controller;

import com.volantum.domain.User;
import com.volantum.dto.LoginRequestDTO;
import com.volantum.dto.RegisterRequestDTO;
import com.volantum.dto.UserResponseDTO;
import com.volantum.service.UserService;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        User user = new User(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(), registerRequest.getPassword());
        User savedUser = userService.register(user);
        return ResponseEntity.ok(userService.convertToDTO(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(userService.convertToDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable int id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(userService.convertToDTO(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}