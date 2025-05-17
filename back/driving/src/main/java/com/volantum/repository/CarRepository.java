package com.volantum.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.volantum.domain.Car;

public interface CarRepository extends CrudRepository<Car, Integer>, PagingAndSortingRepository<Car, Integer> {
	Optional<Car> findByPlate(String plate);
	Page<Car> findAllByUserId(int userId, Pageable pageable);
}
