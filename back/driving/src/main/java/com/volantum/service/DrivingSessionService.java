package com.volantum.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.volantum.domain.Car;
import com.volantum.domain.DrivingSession;
import com.volantum.domain.Event;
import com.volantum.domain.EventType;
import com.volantum.domain.User;
import com.volantum.dto.DrivingSessionRequestDTO;
import com.volantum.dto.DrivingSessionResponseDTO;
import com.volantum.dto.EventRequestDTO;
import com.volantum.repository.CarRepository;
import com.volantum.repository.DrivingSessionRepository;
import com.volantum.repository.EventRepository;
import com.volantum.repository.EventTypeRepository;
import com.volantum.repository.UserRepository;

@Service
public class DrivingSessionService implements DrivingSessionServiceInterface {

	private final DrivingSessionRepository drivingSessionRepository;
	private final EventTypeRepository eventTypeRepository;
	private final ScoreCalculatorService scoreCalculatorService;
	private final UserService userService;
	private final DTOConverterService dtoConverterService;
	private final UserRepository userRepository;
	private final CarRepository carRepository;

	public DrivingSessionService(DrivingSessionRepository repository, EventTypeRepository eventTypeRepository, EventRepository eventRepository, ScoreCalculatorService scoreCalculatorService, UserService userService, DTOConverterService dtoConverterService, UserRepository userRepository, CarRepository carRepository) {
		this.drivingSessionRepository = repository;
		this.eventTypeRepository = eventTypeRepository;
		this.scoreCalculatorService = scoreCalculatorService;
		this.userService = userService;
		this.dtoConverterService = dtoConverterService;
		this.userRepository = userRepository;
		this.carRepository = carRepository;
	}

	/**
	 * Saves a driving session
	 * @param dto the driving session to save
	 * @return the driving session saved
	 */
	public DrivingSession save(DrivingSessionRequestDTO dto) {
		
		User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Car car = carRepository.findById(dto.getCarId()).orElseThrow();

        DrivingSession session = new DrivingSession(user, car);
		String description = switch(LocalDateTime.now().getHour()) {
			case 0, 1, 2, 3, 4, 5, 6 -> "Sesión de madrugada";
			case 7, 8, 9, 10, 11, 12 -> "Sesión en la mañana";
			case 13, 14, 15, 16, 17, 18 -> "Sesión en la tarde";
			default -> "Sesión nocturna";
		};
		session.setDescription(description);
        user.addDrivingSession(session);
		
		return drivingSessionRepository.save(session);
	}

	/**
	 * Finds a driving session by id
	 * @param id the id of the driving session to find
	 * @return the driving session found
	 */
	public Optional<DrivingSession> findById(int id) {
		return drivingSessionRepository.findById(id);
	}

	/**
	 * Finds a driving session by user id
	 * @param userId the id of the user to find the driving sessions of
	 * @return the driving sessions found
	 */
	public List<DrivingSession> findByUserId(int userId) {
		return drivingSessionRepository.findByUserId(userId);
	}

	/**
	 * Finds a driving session by car id
	 * @param carId the id of the car to find the driving sessions of
	 * @return the driving sessions found
	 */
	public List<DrivingSession> findByCarId(int carId) {
		return drivingSessionRepository.findByCarId(carId);
	}


	/**
	 * Updates a driving session with new data.
	 * 
	 * @param id The ID of the session to update.
	 * @param dto The new data for the session.
	 * @return The updated session, calculated with score. Sets the user score if the session is finished.
	 */
	public DrivingSession update(int id, DrivingSessionRequestDTO dto) {
		// 1. Find the session
		DrivingSession session = drivingSessionRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Session not found"));
		
		// 2. Set the end time, distance, duration and average speed
		session.setEndTime(LocalDateTime.now());
		session.setDistance(dto.getDistance());

		Duration duration = Duration.between(session.getStartTime(), session.getEndTime());
		long totalSeconds = duration.getSeconds();
		double hoursFraction = totalSeconds / 3600.0;

		long hours   = totalSeconds / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;
		session.setDuration(String.format("%02d:%02d:%02d", hours, minutes, seconds));

		double speed = 0.0;
		if (hoursFraction > 0) {
			speed = dto.getDistance() / hoursFraction;
		}
		float roundedSpeed = (float) (Math.round(speed * 10.0) / 10.0);
		session.setAverageSpeed(roundedSpeed);
       
		// 3. Set the events
		if (dto.getEvents() != null) {
			for (EventRequestDTO eventDTO : dto.getEvents()) {
				EventType eventType = eventTypeRepository.findById(eventDTO.getEventTypeId()).orElseThrow();
	
				Event event = new Event();
				event.setTimestamp(eventDTO.getTimestamp());
				event.setLatitude(eventDTO.getLatitude());
				event.setLongitude(eventDTO.getLongitude());
				event.setType(eventType);
				event.setDrivingSession(session);
	
				session.addEvent(event);
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

	/**
	 * Deletes all driving sessions
	 */
	public void deleteAll() {
		drivingSessionRepository.deleteAll();
	}

	/**
	 * Finds a driving session by user id and car id
	 * @param userId the id of the user to find the driving sessions of
	 * @param carId the id of the car to find the driving sessions of
	 * @return the driving sessions found
	 */
	public List<DrivingSession> findByUserIdAndCarId(int userId, int carId) {
		return drivingSessionRepository.findByUserIdAndCarId(userId, carId);
	}

	/**
	 * Converts a driving session to a DTO
	 * @param session the driving session to convert
	 * @return the DTO of the driving session
	 */
	public DrivingSessionResponseDTO convertToDTO(DrivingSession session) {
		return dtoConverterService.convertDrivingSessionToDTO(session);
	}
}
