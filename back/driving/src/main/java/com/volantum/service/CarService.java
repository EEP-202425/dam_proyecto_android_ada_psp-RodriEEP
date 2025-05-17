package com.volantum.service;

import java.util.List;
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

	public Car save(Car car) {
		return carRepository.save(car);
	}

	public Optional<Car> findById(int id) {
		return carRepository.findById(id);
	}

	public void deleteById(int id) {
		carRepository.deleteById(id);
	}

	public void deleteAll() {
		carRepository.deleteAll();
	}
	
	public Optional<Car> findByPlate(String plate) {
		return carRepository.findByPlate(plate);
	}
	
	public Page<Car> carsByUserId(int userId, Pageable pageable) {
		return carRepository.findAllByUserId(userId, pageable);
	}

	public CarResponseDTO convertToDTO(Car car) {
		return dtoConverterService.convertCarToDTO(car);
	}

	public Car update(int id, CarRequestDTO car) {
		Car carToUpdate = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
		carToUpdate.setPlate(car.getPlate());
		carToUpdate.setBrand(car.getBrand());
		carToUpdate.setModel(car.getModel());
		carToUpdate.setYearModel(car.getYearModel());
		return carRepository.save(carToUpdate);
	}

	public void delete(int id) {
		carRepository.deleteById(id);
	}
}
