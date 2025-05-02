package com.volantum.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.volantum.domain.Car;
import com.volantum.domain.DrivingSession;
import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;
import com.volantum.service.CarService;
import com.volantum.service.DrivingSessionService;
import com.volantum.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = VolantumApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DrivingSessionControllerTest {

	User testUser;
	Car testCar;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private DrivingSessionService drivingSessionService;

	@Autowired
	private UserService userService;

	@Autowired
	private CarService carService;
	

	@BeforeEach
	void setUp() {
		drivingSessionService.deleteAll();
		carService.deleteAll();
		userService.deleteAll();
		testUser = new User("Laura", "Perez", "laura@volantum.com", "abc123");
		testCar = new Car("XRT234");
	}

	DrivingSession createSession() {
		User user = userService.register(testUser);
		Car car = carService.save(testCar);
		DrivingSession session = new DrivingSession(user, car);
		return session;
	}

	@Test
	void testCreateSession() {
		DrivingSession session = createSession();
		
		ResponseEntity<DrivingSession> response = restTemplate.postForEntity("/api/sessions", session, DrivingSession.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		DrivingSession sessionFromResponse = response.getBody();

		assertEquals(session.getUser().getId(), sessionFromResponse.getUser().getId());
		assertEquals(session.getCar().getId(), sessionFromResponse.getCar().getId());
		assertEquals(session.getStartTime(), sessionFromResponse.getStartTime());
	}

	@Test
	void testGetSessionById() {
		DrivingSession session = createSession();
		DrivingSession savedSession = restTemplate.postForEntity("/api/sessions", session, DrivingSession.class).getBody();
		
		ResponseEntity<DrivingSession> response = restTemplate.getForEntity("/api/sessions/" + savedSession.getId(), DrivingSession.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		DrivingSession sessionFromResponse = response.getBody();

		assertEquals(savedSession.getId(), sessionFromResponse.getId());
		assertEquals(savedSession.getUser().getId(), sessionFromResponse.getUser().getId());
		assertEquals(savedSession.getCar().getId(), sessionFromResponse.getCar().getId());
	}

	@Test
	void testGetSessionsByUserId() {
		DrivingSession session = createSession();

		DrivingSession savedSession = restTemplate.postForEntity("/api/sessions", session, DrivingSession.class).getBody();
		
		ResponseEntity<DrivingSession[]> response = restTemplate.getForEntity("/api/sessions/user/" + testUser.getId(), DrivingSession[].class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		DrivingSession[] sessionsFromResponse = response.getBody();
		
		assertEquals(1, sessionsFromResponse.length);
		assertEquals(savedSession.getId(), sessionsFromResponse[0].getId());
	}

	@Test
	void testGetSessionsByCarId() {
		DrivingSession session = createSession();
		
		DrivingSession savedSession = restTemplate.postForEntity("/api/sessions", session, DrivingSession.class).getBody();

		ResponseEntity<DrivingSession[]> response = restTemplate.getForEntity("/api/sessions/car/" + testCar.getId(), DrivingSession[].class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		DrivingSession[] sessionsFromResponse = response.getBody();
		
		assertEquals(1, sessionsFromResponse.length);
		assertEquals(savedSession.getId(), sessionsFromResponse[0].getId());
	}	

	@Test
	void testFinishSession() {
		DrivingSession session = createSession();
		DrivingSession savedSession = restTemplate.postForEntity("/api/sessions", session, DrivingSession.class).getBody();
		
		savedSession.setDistance(100);

		restTemplate.put("/api/sessions/" + savedSession.getId(), savedSession);

		ResponseEntity<DrivingSession> response = restTemplate.getForEntity("/api/sessions/" + savedSession.getId(), DrivingSession.class);
		DrivingSession updatedSession = response.getBody();

		assertEquals(savedSession.getId(), updatedSession.getId());
		assertEquals(savedSession.getDistance(), updatedSession.getDistance());
		assertNotSame(savedSession.getEndTime(), updatedSession.getEndTime());
	}
	
}
