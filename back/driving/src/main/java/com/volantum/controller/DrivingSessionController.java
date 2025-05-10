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

@RestController
@RequestMapping("/api/sessions")
public class DrivingSessionController {

    private final DrivingSessionService drivingSessionService;

    public DrivingSessionController(DrivingSessionService drivingSessionService) {
        this.drivingSessionService = drivingSessionService;
    }

    @PostMapping
    public ResponseEntity<DrivingSessionResponseDTO> createSession(@RequestBody DrivingSessionRequestDTO session) {
        DrivingSession savedSession = drivingSessionService.save(session);
        return ResponseEntity.ok(drivingSessionService.convertToDTO(savedSession));
    }

    @GetMapping("/user/{userId}")
    public List<DrivingSessionResponseDTO> getSessionsByUser(@PathVariable int userId) {
        return drivingSessionService.findByUserId(userId)
                .stream()
                .map(drivingSessionService::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/car/{carId}")
    public List<DrivingSessionResponseDTO> getSessionsByCar(@PathVariable int carId) {
        return drivingSessionService.findByCarId(carId)
                .stream()
                .map(drivingSessionService::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrivingSessionResponseDTO> getById(@PathVariable int id) {
        Optional<DrivingSession> session = drivingSessionService.findById(id);
        return session
                .map(drivingSessionService::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrivingSessionResponseDTO> finishSession(@PathVariable int id, @RequestBody DrivingSessionRequestDTO sessionDTO) {
        DrivingSession session = drivingSessionService.update(id, sessionDTO);
        return ResponseEntity.ok(drivingSessionService.convertToDTO(session));
    }

}
