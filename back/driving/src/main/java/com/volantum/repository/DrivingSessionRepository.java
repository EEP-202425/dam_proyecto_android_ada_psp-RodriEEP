package com.volantum.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.volantum.domain.DrivingSession;

public interface DrivingSessionRepository extends CrudRepository<DrivingSession, Integer> {
	List<DrivingSession> findByUserId(int userId);
    List<DrivingSession> findByCarId(int carId);
    List<DrivingSession> findByUserIdAndCarId(int userId, int carId);
}
