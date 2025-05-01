package com.volantum.repository;

import org.springframework.data.repository.CrudRepository;

import com.volantum.domain.User;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
