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
import org.springframework.test.context.ActiveProfiles;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;
import com.volantum.dto.DrivingSessionRequestDTO;
import com.volantum.dto.DrivingSessionResponseDTO;
import com.volantum.service.CarService;
import com.volantum.service.DrivingSessionService;
import com.volantum.service.UserService;

@ActiveProfiles("test")
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

	DrivingSessionRequestDTO createSession() {
		User user = userService.register(testUser);
		Car car = carService.save(testCar);
		DrivingSessionRequestDTO session = new DrivingSessionRequestDTO(
			14.3f,
			null,
			user.getId(),
			car.getId());
		return session;
	}

	@Test
	void testCreateSession() {
		DrivingSessionRequestDTO session = createSession();
		
		ResponseEntity<DrivingSessionResponseDTO> response = restTemplate.postForEntity("/api/sessions", session, DrivingSessionResponseDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		DrivingSessionResponseDTO sessionFromResponse = response.getBody();

		assertEquals(session.getCarId(), sessionFromResponse.getCar().getId());
	}

	@Test
	void testGetSessionById() {
		DrivingSessionRequestDTO session = createSession();
		DrivingSessionResponseDTO savedSession = restTemplate.postForEntity("/api/sessions", session, DrivingSessionResponseDTO.class).getBody();
		
		System.out.println(savedSession.getId());
		
		ResponseEntity<DrivingSessionResponseDTO> response = restTemplate.getForEntity("/api/sessions/" + savedSession.getId(), DrivingSessionResponseDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		DrivingSessionResponseDTO sessionFromResponse = response.getBody();

		assertEquals(savedSession.getId(), sessionFromResponse.getId());
		assertEquals(savedSession.getCar().getId(), sessionFromResponse.getCar().getId());
	}

	@Test
	void testGetSessionsByUserId() {
		DrivingSessionRequestDTO session = createSession();

		DrivingSessionResponseDTO savedSession = restTemplate.postForEntity("/api/sessions", session, DrivingSessionResponseDTO.class).getBody();
		
		ResponseEntity<DrivingSessionResponseDTO[]> response = restTemplate.getForEntity("/api/sessions/user/" + testUser.getId(), DrivingSessionResponseDTO[].class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		DrivingSessionResponseDTO[] sessionsFromResponse = response.getBody();
		
		assertEquals(1, sessionsFromResponse.length);
		assertEquals(savedSession.getId(), sessionsFromResponse[0].getId());
	}

	@Test
	void testGetSessionsByCarId() {
		DrivingSessionRequestDTO session = createSession();
		
		DrivingSessionResponseDTO savedSession = restTemplate.postForEntity("/api/sessions", session, DrivingSessionResponseDTO.class).getBody();

		ResponseEntity<DrivingSessionResponseDTO[]> response = restTemplate.getForEntity("/api/sessions/car/" + testCar.getId(), DrivingSessionResponseDTO[].class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		DrivingSessionResponseDTO[] sessionsFromResponse = response.getBody();
		
		assertEquals(1, sessionsFromResponse.length);
		assertEquals(savedSession.getId(), sessionsFromResponse[0].getId());
	}	

	@Test
	void testFinishSession() {
		DrivingSessionRequestDTO session = createSession();
		DrivingSessionResponseDTO savedSession = restTemplate.postForEntity("/api/sessions", session, DrivingSessionResponseDTO.class).getBody();
		
		DrivingSessionRequestDTO updateDTO = new DrivingSessionRequestDTO(
			100f,
			null,
			savedSession.getUser().getId(),
			savedSession.getCar().getId()
		);

		restTemplate.put("/api/sessions/" + savedSession.getId(), updateDTO);

		ResponseEntity<DrivingSessionResponseDTO> response = restTemplate.getForEntity("/api/sessions/" + savedSession.getId(), DrivingSessionResponseDTO.class);
		DrivingSessionResponseDTO updatedSession = response.getBody();

		assertEquals(savedSession.getId(), updatedSession.getId());
		assertEquals(100f, updatedSession.getDistance());
		assertNotSame(savedSession.getEndTime(), updatedSession.getEndTime());
	}
	
}
