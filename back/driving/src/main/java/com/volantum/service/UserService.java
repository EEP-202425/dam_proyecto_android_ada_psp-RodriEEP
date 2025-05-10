package com.volantum.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.volantum.domain.Car;
import com.volantum.domain.DrivingSession;
import com.volantum.domain.User;
import com.volantum.dto.UserResponseDTO;
import com.volantum.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder; 
	private final DTOConverterService dtoConverterService;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DTOConverterService dtoConverterService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.dtoConverterService = dtoConverterService;
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
		return  userRepository.findById(id);
	}

	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	public UserResponseDTO convertToDTO(User user) {
		return dtoConverterService.convertUserToDTO(user);
	}
}
