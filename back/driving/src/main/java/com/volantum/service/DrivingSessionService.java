package com.volantum.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.volantum.domain.DrivingSession;
import com.volantum.repository.DrivingSessionRepository;

@Service
public class DrivingSessionService implements DrivingSessionServiceInterface {

	private final DrivingSessionRepository drivingSessionRepository;

	public DrivingSessionService(DrivingSessionRepository repository) {
		this.drivingSessionRepository = repository;
	}

	public DrivingSession save(DrivingSession session) {
		return drivingSessionRepository.save(session);
	}

	public Optional<DrivingSession> findById(int id) {
		Optional<DrivingSession> session = drivingSessionRepository.findById(id);
		return session;
	}

	public List<DrivingSession> findByUserId(int userId) {
		return drivingSessionRepository.findByUserId(userId);
	}

	public List<DrivingSession> findByCarId(int carId) {
		return drivingSessionRepository.findByCarId(carId);
	}

	public DrivingSession update(int id, DrivingSession session) {
		DrivingSession sessionToUpdate = drivingSessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
		sessionToUpdate.setEndTime(LocalDateTime.now());
		sessionToUpdate.setDistance(session.getDistance());
		return drivingSessionRepository.save(sessionToUpdate);
	}

	public void deleteAll() {
		drivingSessionRepository.deleteAll();
	}

	public List<DrivingSession> findByUserIdAndCarId(int userId, int carId) {
		return drivingSessionRepository.findByUserIdAndCarId(userId, carId);
	}
}
