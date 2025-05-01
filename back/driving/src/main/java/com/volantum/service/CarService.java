package com.volantum.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volantum.domain.Car;
import com.volantum.domain.User;
import com.volantum.repository.CarRepository;

@Service
public class CarService implements CarServiceInterface {
	private final CarRepository carRepository;

	@Autowired
	public CarService(CarRepository carRepository) {
		this.carRepository = carRepository;
	}
	
	public Car addCarToUser(Car car, User user) {
		car.setUser(user);
		user.addCar(car);
		return carRepository.save(car);
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
		List<Car> cars = carRepository.findAllByUserId(userId);
		return cars;
	}

}
