package com.volantum.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.driving.VolantumApplication;
import com.volantum.repository.CarRepository;
import com.volantum.repository.UserRepository;

@SpringBootTest(classes = VolantumApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CarServiceTest {
	
	CarService carService;
	User testUser;
	Car testCar;
	
	@Autowired
	CarRepository carRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@BeforeAll
	void setUpBeforeAll() {
		UserService userService = new UserService(userRepository, passwordEncoder);
		testUser = userService.register(new User("Laura", "Peréz", "laura@volantum.com", "abc123"));
		carService = new CarService(carRepository);
	}
	
	@BeforeEach
	void setUpBeforeEach() {
		carService.deleteAll();
		testCar = new Car("XRT234");
	}
	
	@Test
	void testAddCarToUser() {
		Car savedCar = carService.addCarToUser(testCar, testUser);
		assertEquals(testCar, savedCar);
	}

	@Test
	void findByPlateTest() {
		carService.addCarToUser(testCar, testUser);
		Optional<Car> carOpt = carService.findByPlate(testCar.getPlate());
		assertNotNull(carOpt);
		assertEquals("XRT234", carOpt.get().getPlate());
	}
	
	@Test
	void carsByUserIdTest() {
		carService.addCarToUser(testCar, testUser);
		List<Car> cars = carService.carsByUserId(testUser.getId());
		assertEquals("XRT234", cars.get(0).getPlate());
	}
	
	@Test
	void userCanHaveManyCars() {
		Car testCar2 = new Car("ORM343");
		
		carService.addCarToUser(testCar, testUser);
		carService.addCarToUser(testCar2, testUser);
		
		List<Car> cars = carService.carsByUserId(testUser.getId());

		assertEquals(2, cars.size());
	}

}
