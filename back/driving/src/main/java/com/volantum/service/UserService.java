package com.volantum.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.volantum.domain.User;
import com.volantum.dto.UserResponseDTO;
import com.volantum.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder; 

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User register(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public User login(String email, String password) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
			return userOpt.get();
		} else {
			throw new RuntimeException("Credenciales Inválidas");
		}
	}

	@Override
	public User updateProfile(User user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Optional<User> findById(int id) {
		return userRepository.findById(id);
	}

	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	public UserResponseDTO convertToDTO(User user) {
		return new UserResponseDTO(
			user.getId(),
			user.getFirstName(),
			user.getLastName(),
			user.getEmail(),
			user.getScore()
		);
	}
}
