package com.volantum.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.dto.CarRequestDTO;
import com.volantum.dto.CarResponseDTO;
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
	

	@GetMapping("/{id}" )
	public ResponseEntity<CarResponseDTO> carById(@PathVariable int id) {
		Optional<Car> car = carService.findById(id);
		if (car.isPresent()) {
			return ResponseEntity.ok(carService.convertToDTO(car.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<CarResponseDTO>> carsByUser(@PathVariable int userId) {
		List<Car> cars = carService.carsByUserId(userId);
		return ResponseEntity.ok(cars.stream()
			.map(carService::convertToDTO)
			.collect(Collectors.toList()));
	}

	@PostMapping("/user/{userId}")
	public ResponseEntity<CarResponseDTO> addCarToUser(@PathVariable int userId, @RequestBody CarRequestDTO car) {
        Optional<User> user = userService.findById(userId);
		if (user.isPresent()) {
			Car savedCar = carService.addCarToUser(car, user.get());
			return ResponseEntity.ok(carService.convertToDTO(savedCar));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
