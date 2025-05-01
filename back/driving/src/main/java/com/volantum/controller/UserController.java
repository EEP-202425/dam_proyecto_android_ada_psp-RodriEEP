package com.volantum.controller;

import com.volantum.domain.User;
import com.volantum.dto.LoginRequest;
import com.volantum.service.UserService;
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
    public ResponseEntity<User> register(@RequestBody User user) {
        User savedUser = userService.register(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(user);
    }
}