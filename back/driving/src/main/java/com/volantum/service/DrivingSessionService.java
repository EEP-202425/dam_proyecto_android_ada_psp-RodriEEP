package com.volantum.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.volantum.domain.DrivingSession;
import com.volantum.domain.Event;
import com.volantum.domain.User;
import com.volantum.repository.DrivingSessionRepository;
import com.volantum.repository.EventRepository;
import com.volantum.repository.EventTypeRepository;

@Service
public class DrivingSessionService implements DrivingSessionServiceInterface {

	private final DrivingSessionRepository drivingSessionRepository;
	private final EventRepository eventRepository;
	private final ScoreCalculatorService scoreCalculatorService;
	private final UserService userService;

	public DrivingSessionService(DrivingSessionRepository repository, EventTypeRepository eventTypeRepository, EventRepository eventRepository, ScoreCalculatorService scoreCalculatorService, UserService userService) {
		this.drivingSessionRepository = repository;
		this.eventRepository = eventRepository;
		this.scoreCalculatorService = scoreCalculatorService;
		this.userService = userService;
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


	/**
	 * Updates a driving session with new data.
	 * 
	 * @param id The ID of the session to update.
	 * @param updatedSessionData The new data for the session.
	 * @return The updated session, calculated with score. Sets the user score if the session is finished.
	 */
	public DrivingSession update(int id, DrivingSession updatedSessionData) {
		// 1. Find the session
		DrivingSession session = drivingSessionRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Session not found"));
		
		// 2. Set the end time and distance
		session.setEndTime(LocalDateTime.now());
		session.setDistance(updatedSessionData.getDistance());

		// 3. Set the events
		if (updatedSessionData.getEvents() != null) {
			for (Event event : updatedSessionData.getEvents()) {
				event.setDrivingSession(session);
				Event savedEvent = eventRepository.save(event);
				session.addEvent(savedEvent);
			}

			// 4. Calculate the session score
			float sessionScore = scoreCalculatorService.calculateSessionScore(session.getEvents());
			session.setScore(sessionScore);

			// 5. Calculate the user score
			User user = session.getUser();
			List<DrivingSession> userSessions = drivingSessionRepository.findByUserId(user.getId());
			float userScore = scoreCalculatorService.calculateUserScore(userSessions);
			user.setScore(userScore);
			userService.updateProfile(user);
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
