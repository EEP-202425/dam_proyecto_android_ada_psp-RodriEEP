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
		ResponseEntity<Car> response = restTemplate.postForEntity("/api/cars", testCar, Car.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		Car carSaved = response.getBody();
		assertEquals(testCar.getPlate(), carSaved.getPlate());
	}

	@Test
	void testCarById() {
		ResponseEntity<Car> response = restTemplate.postForEntity("/api/cars", testCar, Car.class);
		Car carSaved = response.getBody();
		ResponseEntity<Car> responseById = restTemplate.getForEntity("/api/cars/" + carSaved.getId(), Car.class);
		assertEquals(HttpStatus.OK, responseById.getStatusCode());

		Car carFounded = response.getBody();
		assertNotNull(carFounded);
		
		assertEquals(testCar.getPlate(), carFounded.getPlate());
	}

	@Test
	void testAddCarToUser() {
		ResponseEntity<Car> response = restTemplate.postForEntity("/api/cars/user/" + testUser.getId(), testCar, Car.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		Car carSaved = response.getBody();
		
		assertEquals(testCar.getPlate(), carSaved.getPlate());
	}

	@Test
	void testCarsByUser() {
		restTemplate.postForEntity("/api/cars/user/" + testUser.getId(), testCar, Car.class);
		ResponseEntity<Car[]> response = restTemplate.getForEntity("/api/cars/user/" + testUser.getId(), Car[].class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
				
		Car[] carsFromResponse = response.getBody();
		assertNotNull(carsFromResponse);
		
		assertEquals(1, carsFromResponse.length);
	}

	@Test
	void testAddCarToUserWithInvalidUser() {
		ResponseEntity<Car> response = restTemplate.postForEntity("/api/cars/user/999", testCar, Car.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

}
