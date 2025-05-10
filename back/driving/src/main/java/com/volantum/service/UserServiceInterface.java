package com.volantum.service;

import java.util.Optional;

import com.volantum.domain.User;
import com.volantum.dto.UserResponseDTO;

public interface UserServiceInterface {
	User register(User user);
    User login(String email, String password);
    User updateProfile(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    void deleteAll();
    UserResponseDTO convertToDTO(User user);
}
