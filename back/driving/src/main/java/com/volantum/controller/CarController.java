package com.volantum.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.service.CarService;
import com.volantum.service.UserService;

@RestController
@RequestMapping("/api/cars")
public class CarController {
	
	private final CarService carService;
    private final UserService userService;
	
	public CarController(CarService carService, UserService userService) {
		this.carService = carService;
		this.userService = userService;
	}
	

    @PostMapping
    public ResponseEntity<Car> saveCar(@RequestBody Car car) {
        Car savedCar = carService.save(car);
        return ResponseEntity.ok(savedCar);
    }

	@GetMapping("/{id}" )
	public ResponseEntity<Car> carById(@PathVariable int id) {
		Optional<Car> car = carService.findById(id);
		if (car.isPresent()) {
			return ResponseEntity.ok(car.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Car>> carsByUser(@PathVariable int userId) {
		List<Car> cars = carService.carsByUserId(userId);
		System.out.println("Cars founded" + cars);
		return ResponseEntity.ok(cars);
	}

	@PostMapping("/user/{userId}")
	public ResponseEntity<Car> addCarToUser(@PathVariable int userId, @RequestBody Car car) {
        Optional<User> user = userService.findById(userId);
		if (user.isPresent()) {
			Car savedCar = carService.addCarToUser(car, user.get());
			return ResponseEntity.ok(savedCar);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
