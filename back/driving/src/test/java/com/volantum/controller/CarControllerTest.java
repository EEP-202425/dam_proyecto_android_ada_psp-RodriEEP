package com.volantum.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.volantum.dto.CarResponseDTO;
import com.volantum.service.CarService;
import com.volantum.service.UserService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = VolantumApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarControllerTest {

	private User testUser;
	private Car testCar;

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	private CarService carService;

	@Autowired
	private UserService userService;
		
	@BeforeEach
	void setUpBeforeEach() {	
		carService.deleteAll();
		userService.deleteAll();
		testUser = userService.register(new User("Laura", "Peréz", "laura@volantum.com", "abc123"));
		testCar = new Car("XRT234");
	}

    @Test
	void testSaveCar() {
		ResponseEntity<CarResponseDTO> response = restTemplate.postForEntity("/api/cars/user/" + testUser.getId(), testCar, CarResponseDTO.class, testUser.getId());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		CarResponseDTO carSaved = response.getBody();
		assertEquals(testCar.getPlate(), carSaved.getPlate());
	}

	@Test
	void testCarById() {
		ResponseEntity<CarResponseDTO> response = restTemplate.postForEntity("/api/cars/user/" + testUser.getId(), testCar, CarResponseDTO.class);
		CarResponseDTO carSaved = response.getBody();
		ResponseEntity<CarResponseDTO> responseById = restTemplate.getForEntity("/api/cars/" + carSaved.getId(), CarResponseDTO.class);
		assertEquals(HttpStatus.OK, responseById.getStatusCode());

		CarResponseDTO carFounded = responseById.getBody();
		assertNotNull(carFounded);
		
		assertEquals(testCar.getPlate(), carFounded.getPlate());
	}

	@Test
	void testAddCarToUser() {
		ResponseEntity<CarResponseDTO> response = restTemplate.postForEntity("/api/cars/user/" + testUser.getId(), testCar, CarResponseDTO.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		CarResponseDTO carSaved = response.getBody();
		
		assertEquals(testCar.getPlate(), carSaved.getPlate());
	}

	@Test
	void testCarsByUser() {
		restTemplate.postForEntity("/api/cars/user/" + testUser.getId(), testCar, CarResponseDTO.class);
		ResponseEntity<CarResponseDTO[]> response = restTemplate.getForEntity("/api/cars/user/" + testUser.getId(), CarResponseDTO[].class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
				
		CarResponseDTO[] carsFromResponse = response.getBody();
		assertNotNull(carsFromResponse);
		
		assertEquals(1, carsFromResponse.length);
	}

	@Test
	void testAddCarToUserWithInvalidUser() {
		ResponseEntity<CarResponseDTO> response = restTemplate.postForEntity("/api/cars/user/999", testCar, CarResponseDTO.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}
