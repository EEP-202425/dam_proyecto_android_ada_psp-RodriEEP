package com.volantum.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;

@SpringBootTest(classes = VolantumApplication.class)
public class UserServiceTest {

	private User testUser;
	
	@Autowired
	private UserService userService;

	@BeforeEach
	void setUp() {
		userService.deleteAll();
		testUser = new User("Laura", "Peréz", "laura@volantum.com", "abc123");
	}
	
	@Test
	void registerAndFindByEmailTest() {
		userService.register(testUser);

		Optional<User> userOpt = userService.findByEmail(testUser.getEmail());

		assertTrue(userOpt.isPresent());
		assertEquals("Laura", userOpt.get().getFirstName());
	}

	@Test
	void loginSuccessfulTest() {
		userService.register(testUser);

		User loggedIn = userService.login(testUser.getEmail(), "abc123");
		
		assertNotNull(loggedIn);
		assertEquals("Laura", loggedIn.getFirstName());
		assertEquals("Peréz", loggedIn.getLastName());
		assertEquals("laura@volantum.com", loggedIn.getEmail());
	}

	@Test
	void loginFailedTest() {
		userService.register(testUser);

		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> userService.login(testUser.getEmail(), "12345678"));
		assertEquals("Credenciales Inválidas", ex.getMessage());
	}

}
