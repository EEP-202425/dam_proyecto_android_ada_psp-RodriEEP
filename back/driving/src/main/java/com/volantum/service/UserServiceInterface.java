package com.volantum.service;

import java.util.Optional;

import com.volantum.domain.User;

public interface UserServiceInterface {
	User register(User user);
    User login(String email, String password);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    void deleteAll();
}
