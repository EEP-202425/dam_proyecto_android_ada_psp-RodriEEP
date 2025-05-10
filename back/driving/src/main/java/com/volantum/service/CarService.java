package com.volantum.service;

import java.util.List;
import java.util.Optional;

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

	@Autowired
	public CarService(CarRepository carRepository) {
		this.carRepository = carRepository;
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
	
	public List<Car> carsByUserId(int userId) {
		return carRepository.findAllByUserId(userId);
	}

	public CarResponseDTO convertToDTO(Car car) {
		return new CarResponseDTO(
			car.getId(),
			car.getPlate(),
			car.getBrand(),
			car.getModel(),
			car.getYearModel(),
			car.getImage(),
			car.getMileage()
		);
	}

}
