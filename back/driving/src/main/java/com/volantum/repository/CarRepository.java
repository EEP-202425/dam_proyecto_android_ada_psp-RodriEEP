package com.volantum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.volantum.domain.Car;

public interface CarRepository extends CrudRepository<Car, Integer> {
	Optional<Car> findByPlate(String plate);
	List<Car> findAllByUserId(int userId);
}
