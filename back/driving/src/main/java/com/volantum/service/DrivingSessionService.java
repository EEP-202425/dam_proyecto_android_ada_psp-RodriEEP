package com.volantum.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.volantum.domain.DrivingSession;
import com.volantum.domain.Event;
import com.volantum.repository.DrivingSessionRepository;
import com.volantum.repository.EventRepository;
import com.volantum.repository.EventTypeRepository;

@Service
public class DrivingSessionService implements DrivingSessionServiceInterface {

	private final DrivingSessionRepository drivingSessionRepository;
	private final EventRepository eventRepository;

	public DrivingSessionService(DrivingSessionRepository repository, EventTypeRepository eventTypeRepository, EventRepository eventRepository) {
		this.drivingSessionRepository = repository;
		this.eventRepository = eventRepository;
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

	public DrivingSession update(int id, DrivingSession updatedSessionData) {
		DrivingSession session = drivingSessionRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Session not found"));
		
		session.setEndTime(LocalDateTime.now());
		session.setDistance(updatedSessionData.getDistance());
		
		if (updatedSessionData.getEvents() != null) {
			for (Event event : updatedSessionData.getEvents()) {
				event.setDrivingSession(session);
				Event savedEvent = eventRepository.save(event);
				session.addEvent(savedEvent);
			}
		}
		
		return drivingSessionRepository.save(session);
	}

	public void deleteAll() {
		drivingSessionRepository.deleteAll();
	}

	public List<DrivingSession> findByUserIdAndCarId(int userId, int carId) {
		return drivingSessionRepository.findByUserIdAndCarId(userId, carId);
	}
}
