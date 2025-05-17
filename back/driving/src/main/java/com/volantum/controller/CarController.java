package com.volantum.controller;

import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.dto.CarRequestDTO;
import com.volantum.dto.CarResponseDTO;
import com.volantum.service.CarService;
import com.volantum.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cars")
@Tag(name = "Cars", description = "API for managing cars and their associated driving sessions")
public class CarController {
	
	private final CarService carService;
    private final UserService userService;
	
	public CarController(CarService carService, UserService userService) {
		this.carService = carService;
		this.userService = userService;
	}
	
	/**
	 * Gets a car by id
	 * @param id the id of the car to get
	 * @return the car found
	 */
	@GetMapping("/{id}" )
	@Operation(summary = "Gets a car by id", description = "Gets a car by id")
	public ResponseEntity<CarResponseDTO> carById(
		@Parameter(description = "The id of the car to get")
		@PathVariable int id
	) {
		Optional<Car> car = carService.findById(id);
		if (car.isPresent()) {
			return ResponseEntity.ok(carService.convertToDTO(car.get()));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Gets all cars by user id
	 * @param userId the id of the user to get the cars of
	 * @param pageable the pageable to paginate the cars
	 * @return the cars found
	 */
	@GetMapping("/user/{userId}")
	@Operation(summary = "Gets all cars by user id", description = "Gets all cars by user id")
	public ResponseEntity<Page<CarResponseDTO>> carsByUser(
		@Parameter(description = "The id of the user to get the cars of")
		@PathVariable int userId, 
		@ParameterObject Pageable pageable
	) {		
		Page<Car> cars = carService.carsByUserId(userId, pageable);
		Page<CarResponseDTO> dtoPage = cars.map(carService::convertToDTO);
		return ResponseEntity.ok(dtoPage);
	}

	/**
	 * Adds a car to a user
	 * @param userId the id of the user to add the car to
	 * @param car the car to add
	 * @return the car added
	 */	
	@PostMapping("/user/{userId}")
	@Operation(summary = "Adds a car to a user", description = "Adds a car to a user with the given request")
	public ResponseEntity<CarResponseDTO> addCarToUser(
		@Parameter(description = "The id of the user to add the car to")
		@PathVariable int userId, 
		@RequestBody CarRequestDTO car
	) {
        Optional<User> user = userService.findById(userId);
		if (user.isPresent()) {
			Car savedCar = carService.addCarToUser(car, user.get());
			return ResponseEntity.ok(carService.convertToDTO(savedCar));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * Updates a car
	 * @param id the id of the car to update
	 * @param car the car to update
	 * @return the car updated
	 */
	@PutMapping("/{id}")
	@Operation(summary = "Updates a car", description = "Updates a car with the given request")
	public ResponseEntity<CarResponseDTO> updateCar(
		@Parameter(description = "The id of the car to update")
		@PathVariable int id, 
		@RequestBody CarRequestDTO car
	) {
		Car updatedCar = carService.update(id, car);
		return ResponseEntity.ok(carService.convertToDTO(updatedCar));
	}

	/**
	 * Deletes a car
	 * @param id the id of the car to delete
	 * @return the car deleted
	 */
	@DeleteMapping("/{id}")
	@Operation(summary = "Deletes a car", description = "Deletes a car with the given id")
	public ResponseEntity<Void> deleteCar(
		@Parameter(description = "The id of the car to delete")
		@PathVariable int id
	) {
		carService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
