package com.volantum.controller;

import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;
import com.volantum.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = VolantumApplication.class)
class UserControllerTest {

	private User testUser;

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	private UserService userService;

	@BeforeEach
	void setUp() {
		userService.deleteAll();
		testUser = new User("Laura", "Peréz", "laura@volantum.com", "abc123");
	}

	@Test
	void testRegister() {
		ResponseEntity<User> registerResponse = restTemplate.postForEntity("/api/users/register", testUser, User.class);

		assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		User returnedUser = registerResponse.getBody();
		assertThat(returnedUser).isNotNull();
		assertThat(returnedUser.getEmail()).isEqualTo(testUser.getEmail());
		assertThat(returnedUser.getFirstName()).isEqualTo(testUser.getFirstName());
		assertThat(returnedUser.getLastName()).isEqualTo(testUser.getLastName());
	}

	@Test
	void testLogin() {
		userService.register(testUser);

		Map<String, String> loginRequest = new HashMap<>();
		loginRequest.put("email", testUser.getEmail());
		loginRequest.put("password", "abc123");

		ResponseEntity<User> loginResponse = restTemplate.postForEntity("/api/users/login", loginRequest, User.class);

		assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		User returnedUser = loginResponse.getBody();
		assertThat(returnedUser).isNotNull();
		assertThat(returnedUser.getEmail()).isEqualTo(testUser.getEmail());
		assertThat(returnedUser.getFirstName()).isEqualTo(testUser.getFirstName());
		assertThat(returnedUser.getLastName()).isEqualTo(testUser.getLastName());
	}
}
