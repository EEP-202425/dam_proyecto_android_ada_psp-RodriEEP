package com.volantum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;
import com.volantum.dto.CarRequestDTO;

@ActiveProfiles("test")
@SpringBootTest(classes = VolantumApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarServiceTest {
	User testUser;
	CarRequestDTO carTest;
	
	@Autowired
	CarService carService;

	@Autowired
	UserService userService;
	
	@BeforeAll
	void setUpBeforeAll() {
		testUser = userService.register(new User("Laura", "Peréz", "laura@volantum.com", "abc123"));
	}
	
	@BeforeEach
	void setUpBeforeEach() {
		carService.deleteAll();
		carTest = new CarRequestDTO("XRT234", "Toyota", "Corolla", 2020, null, 0.0);
	}
	
	@Test
	void testAddCarToUser() {
		Car savedCar = carService.addCarToUser(carTest, testUser);
		assertEquals(carTest.getPlate(), savedCar.getPlate());
	}

	@Test
	void findByPlateTest() {
		carService.addCarToUser(carTest, testUser);
		Optional<Car> carOpt = carService.findByPlate(carTest.getPlate());
		assertNotNull(carOpt);
		assertEquals(carTest.getPlate(), carOpt.get().getPlate());
	}
	
	@Test
	void carsByUserIdTest() {
		carService.addCarToUser(carTest, testUser);
		List<Car> cars = carService.carsByUserId(testUser.getId());
		assertEquals(carTest.getPlate(), cars.get(0).getPlate());
	}
	
	@Test
	void userCanHaveManyCars() {
		carService.addCarToUser(carTest, testUser);
		CarRequestDTO carTest2 = new CarRequestDTO("ORM343", "Toyota", "Corolla", 2020, null, 0.0);
		carService.addCarToUser(carTest2, testUser);
		
		List<Car> cars = carService.carsByUserId(testUser.getId());

		assertEquals(2, cars.size());
	}

}
