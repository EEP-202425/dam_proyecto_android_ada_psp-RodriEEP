package com.volantum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;
import com.volantum.dto.CarRequestDTO;
import com.volantum.dto.DrivingSessionRequestDTO;

import jakarta.transaction.Transactional;

@ActiveProfiles("test")
@SpringBootTest(classes = VolantumApplication.class)
@Transactional
public class UserServiceTest {

	private User testUser;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CarService carService;

	@Autowired
	private DrivingSessionService drivingSessionService;

	@BeforeEach
	void setUp() {
		userService.deleteAll();
		carService.deleteAll();
		drivingSessionService.deleteAll();
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

	@Test
	void testUserProfile() {
		userService.register(testUser);

		CarRequestDTO car = new CarRequestDTO("XRT234", "Toyota", "Corolla", 2020, null, 0.0);
		Car savedCar = carService.addCarToUser(car, testUser);

		DrivingSessionRequestDTO session1 = new DrivingSessionRequestDTO(0.0f, null, testUser.getId(), savedCar.getId());
		drivingSessionService.save(session1);

		DrivingSessionRequestDTO session2 = new DrivingSessionRequestDTO(0.0f, null, testUser.getId(), savedCar.getId());
		drivingSessionService.save(session2);

		User user = userService.findByEmail(testUser.getEmail()).orElseThrow();

		assertEquals("Laura", user.getFirstName());
		assertEquals("Peréz", user.getLastName());
		assertEquals("laura@volantum.com", user.getEmail());

		assertEquals(1, user.getCars().size());
		assertEquals(2, user.getDrivingSessions().size());
	}

}
