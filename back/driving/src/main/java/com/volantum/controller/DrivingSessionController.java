package com.volantum.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volantum.domain.DrivingSession;
import com.volantum.dto.DrivingSessionRequestDTO;
import com.volantum.dto.DrivingSessionResponseDTO;
import com.volantum.service.DrivingSessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Driving Sessions", description = "API for managing driving sessions (start, finish, list sessions by user or car)")
public class DrivingSessionController {

    private final DrivingSessionService drivingSessionService;

    public DrivingSessionController(DrivingSessionService drivingSessionService) {
        this.drivingSessionService = drivingSessionService;
    }

    /**
     * Creates a new driving session
     * @param session the driving session to create
     * @return the driving session created
     */
    @Operation(summary = "Creates a new driving session", description = "Creates a new driving session with the given request")
    @PostMapping
    public ResponseEntity<DrivingSessionResponseDTO> createSession(
		@Parameter(description = "The driving session to create")
		@RequestBody DrivingSessionRequestDTO session
	) {
        DrivingSession savedSession = drivingSessionService.save(session);
        return ResponseEntity.ok(drivingSessionService.convertToDTO(savedSession));
    }

    /**
     * Gets all driving sessions by user id
     * @param userId the id of the user to get the driving sessions of
     * @return the driving sessions found
     */
    @Operation(summary = "Gets all driving sessions by user id", description = "Gets all driving sessions by user id")
    @GetMapping("/user/{userId}")
    public List<DrivingSessionResponseDTO> getSessionsByUser(
		@Parameter(description = "The id of the user to get the driving sessions of")
		@PathVariable int userId
	) {
        return drivingSessionService.findByUserId(userId)
                .stream()
                .map(drivingSessionService::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets all driving sessions by car id
     * @param carId the id of the car to get the driving sessions of
     * @return the driving sessions found
     */
    @Operation(summary = "Gets all driving sessions by car id", description = "Gets all driving sessions by car id")
    @GetMapping("/car/{carId}")
    public List<DrivingSessionResponseDTO> getSessionsByCar(
		@Parameter(description = "The id of the car to get the driving sessions of")
		@PathVariable int carId
	) {
        return drivingSessionService.findByCarId(carId)
                .stream()
                .map(drivingSessionService::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets a driving session by id
     * @param id the id of the driving session to get
     * @return the driving session found
     */
    @Operation(summary = "Gets a driving session by id", description = "Gets a driving session by id")
    @GetMapping("/{id}")
    public ResponseEntity<DrivingSessionResponseDTO> getById(
		@Parameter(description = "The id of the driving session to get")
		@PathVariable int id
	) {
        Optional<DrivingSession> session = drivingSessionService.findById(id);
        return session
                .map(drivingSessionService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Finishes a driving session
     * @param id the id of the driving session to finish
     * @param sessionDTO the driving session to finish
     * @return the driving session finished
     */
    @Operation(summary = "Finishes a driving session", description = "Finishes a driving session with the given request")
    @PutMapping("/{id}")
    public ResponseEntity<DrivingSessionResponseDTO> finishSession(
		@Parameter(description = "The id of the driving session to finish")
		@PathVariable int id, 
		@Parameter(description = "Final driving session data")
		@RequestBody DrivingSessionRequestDTO sessionDTO
	) {
        DrivingSession session = drivingSessionService.update(id, sessionDTO);
        return ResponseEntity.ok(drivingSessionService.convertToDTO(session));
    }

}
