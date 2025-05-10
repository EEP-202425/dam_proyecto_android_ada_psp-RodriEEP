package com.volantum.service;

import java.util.List;

import com.volantum.domain.DrivingSession;
public interface DrivingSessionServiceInterface {

	List<DrivingSession> findByUserId(int userId);
    List<DrivingSession> findByCarId(int carId);
    List<DrivingSession> findByUserIdAndCarId(int userId, int carId);
}
