package com.volantum.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.volantum.domain.Car;
import com.volantum.domain.DrivingSession;
import com.volantum.domain.Event;
import com.volantum.domain.EventType;
import com.volantum.domain.User;
import com.volantum.dto.DrivingSessionRequestDTO;
import com.volantum.dto.DrivingSessionResponseDTO;
import com.volantum.dto.EventRequestDTO;
import com.volantum.dto.EventResponseDTO;
import com.volantum.dto.EventTypeResponseDTO;
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
	private final CarService carService;
	private final UserRepository userRepository;
	private final CarRepository carRepository;

	public DrivingSessionService(DrivingSessionRepository repository, EventTypeRepository eventTypeRepository, EventRepository eventRepository, ScoreCalculatorService scoreCalculatorService, UserService userService, CarService carService, UserRepository userRepository, CarRepository carRepository) {
		this.drivingSessionRepository = repository;
		this.eventTypeRepository = eventTypeRepository;
		this.scoreCalculatorService = scoreCalculatorService;
		this.userService = userService;
		this.carService = carService;
		this.userRepository = userRepository;
		this.carRepository = carRepository;
	}

	public DrivingSession save(DrivingSessionRequestDTO dto) {
		
		User user = userRepository.findById(dto.getUserId()).orElseThrow();
        Car car = carRepository.findById(dto.getCarId()).orElseThrow();

        DrivingSession session = new DrivingSession(user, car);
		
		return drivingSessionRepository.save(session);
	}

	public Optional<DrivingSession> findById(int id) {
		return drivingSessionRepository.findById(id);
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
	 * @param dto The new data for the session.
	 * @return The updated session, calculated with score. Sets the user score if the session is finished.
	 */
	public DrivingSession update(int id, DrivingSessionRequestDTO dto) {
		// 1. Find the session
		DrivingSession session = drivingSessionRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Session not found"));
		
		// 2. Set the end time and distance
		session.setEndTime(LocalDateTime.now());
		session.setDistance(dto.getDistance());

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

	public void deleteAll() {
		drivingSessionRepository.deleteAll();
	}

	public List<DrivingSession> findByUserIdAndCarId(int userId, int carId) {
		return drivingSessionRepository.findByUserIdAndCarId(userId, carId);
	}

	public DrivingSessionResponseDTO convertToDTO(DrivingSession session) {
		DrivingSessionResponseDTO dto = new DrivingSessionResponseDTO();
		dto.setId(session.getId());
		dto.setStartTime(session.getStartTime());
		dto.setEndTime(session.getEndTime());
		dto.setDistance(session.getDistance());
		dto.setScore(session.getScore());
		dto.setEvents(session.getEvents().stream().map(this::convertEventToDTO).collect(Collectors.toList()));
		dto.setUser(userService.convertToDTO(session.getUser()));
		dto.setCar(carService.convertToDTO(session.getCar()));
		return dto;
	}

	public EventResponseDTO convertEventToDTO(Event event) {
		EventResponseDTO dto = new EventResponseDTO();
		dto.setId(event.getId());
		dto.setTimestamp(event.getTimestamp());
		dto.setLatitude(event.getLatitude());
		dto.setLongitude(event.getLongitude());
		dto.setType(convertEventTypeToDTO(event.getType()));
		return dto;
	}

	public EventTypeResponseDTO convertEventTypeToDTO(EventType eventType) {
		EventTypeResponseDTO dto = new EventTypeResponseDTO();
		dto.setId(eventType.getId());
		dto.setName(eventType.getName());
		dto.setDescription(eventType.getDescription());
		dto.setSeverity(eventType.getSeverity());
		return dto;
	}
}
