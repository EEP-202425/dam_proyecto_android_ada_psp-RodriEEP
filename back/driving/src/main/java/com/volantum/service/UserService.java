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
	private final DTOConverterService dtoConverterService;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, DTOConverterService dtoConverterService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.dtoConverterService = dtoConverterService;
	}

	/**
	 * Registers a user
	 * @param user the user to register
	 * @return the user registered
	 */
	@Override
	public User register(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	/**
	 * Logs in a user
	 * @param email the email of the user to login
	 * @param password the password of the user to login
	 * @return the user logged in
	 */
	@Override
	public User login(String email, String password) {
		Optional<User> userOpt = userRepository.findByEmail(email);
		if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
			return userOpt.get();
		} else {
			throw new RuntimeException("Credenciales Inválidas");
		}
	}

	/**
	 * Updates a user's profile
	 * @param user the user to update
	 * @return the user updated
	 */
	@Override
	public User updateProfile(User user) {
		return userRepository.save(user);
	}

	/**
	 * Finds a user by email
	 * @param email the email of the user to find
	 * @return the user found
	 */
	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Finds a user by id
	 * @param id the id of the user to find
	 * @return the user found
	 */
	@Override
	public Optional<User> findById(int id) {
		return  userRepository.findById(id);
	}

	/**
	 * Deletes all users
	 */
	@Override
	public void deleteAll() {
		userRepository.deleteAll();
	}

	/**
	 * Converts a user to a DTO
	 * @param user the user to convert
	 * @return the DTO of the user
	 */
	@Override
	public UserResponseDTO convertToDTO(User user) {
		return dtoConverterService.convertUserToDTO(user);
	}
}
