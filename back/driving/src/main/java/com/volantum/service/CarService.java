package com.volantum.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.dto.CarRequestDTO;
import com.volantum.dto.CarResponseDTO;
import com.volantum.repository.CarRepository;

@Service
public class CarService implements CarServiceInterface {
	private final CarRepository carRepository;
	private final DTOConverterService dtoConverterService;

	@Autowired
	public CarService(CarRepository carRepository, DTOConverterService dtoConverterService) {
		this.carRepository = carRepository;
		this.dtoConverterService = dtoConverterService;
	}

	/**
	 * Adds a car to a user
	 * @param car the car to add
	 * @param user the user to add the car to
	 * @return the car added
	 */
	
	public Car addCarToUser(CarRequestDTO car, User user) {
		Car newCar = new Car(
			car.getPlate(),
			car.getBrand(),
			car.getModel(),
			car.getYearModel()
		);
		newCar.setUser(user);
		user.addCar(newCar);
		return carRepository.save(newCar);
	}

	/**
	 * Saves a car
	 * @param car the car to save
	 * @return the car saved
	 */
	public Car save(Car car) {
		return carRepository.save(car);
	}

	/**
	 * Finds a car by id
	 * @param id the id of the car to find
	 * @return the car found
	 */
	public Optional<Car> findById(int id) {
		return carRepository.findById(id);
	}

	/**
	 * Deletes a car by id
	 * @param id the id of the car to delete
	 */
	public void deleteById(int id) {
		carRepository.deleteById(id);
	}

	/**
	 * Deletes all cars
	 */
	public void deleteAll() {
		carRepository.deleteAll();
	}

	/**
	 * Finds a car by plate
	 * @param plate the plate of the car to find
	 * @return the car found
	 */
	public Optional<Car> findByPlate(String plate) {
		return carRepository.findByPlate(plate);
	}

	/**
	 * Finds all cars by user id
	 * @param userId the id of the user to find the cars of
	 * @param pageable the pageable to paginate the cars
	 * @return the cars found
	 */
	public Page<Car> carsByUserId(int userId, Pageable pageable) {
		return carRepository.findAllByUserId(userId, pageable);
	}

	/**
	 * Converts a car to a DTO
	 * @param car the car to convert
	 * @return the DTO of the car
	 */
	public CarResponseDTO convertToDTO(Car car) {
		return dtoConverterService.convertCarToDTO(car);
	}

	/**
	 * Updates a car
	 * @param id the id of the car to update
	 * @param car the car to update
	 * @return the updated car
	 */
	public Car update(int id, CarRequestDTO car) {
		Car carToUpdate = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
		carToUpdate.setPlate(car.getPlate());
		carToUpdate.setBrand(car.getBrand());
		carToUpdate.setModel(car.getModel());
		carToUpdate.setYearModel(car.getYearModel());
		return carRepository.save(carToUpdate);
	}

	/**
	 * Deletes a car by id
	 * @param id the id of the car to delete
	 */
	public void delete(int id) {
		carRepository.deleteById(id);
	}
}
