package com.volantum.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.volantum.domain.DrivingSession;
import com.volantum.service.DrivingSessionService;

@RestController
@RequestMapping("/api/sessions")
public class DrivingSessionController {

    private final DrivingSessionService drivingSessionService;

    public DrivingSessionController(DrivingSessionService drivingSessionService) {
        this.drivingSessionService = drivingSessionService;
    }

    @PostMapping
    public ResponseEntity<DrivingSession> createSession(@RequestBody DrivingSession session) {
        return ResponseEntity.ok(drivingSessionService.save(session));
    }

    @GetMapping("/user/{userId}")
    public List<DrivingSession> getSessionsByUser(@PathVariable int userId) {
        return drivingSessionService.findByUserId(userId);
    }

    @GetMapping("/car/{carId}")
    public List<DrivingSession> getSessionsByCar(@PathVariable int carId) {
        return drivingSessionService.findByCarId(carId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrivingSession> getById(@PathVariable int id) {
        Optional<DrivingSession> session = drivingSessionService.findById(id);
        return session
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DrivingSession> finishSession(@PathVariable int id, @RequestBody DrivingSession session) {
        return ResponseEntity.ok(drivingSessionService.update(id, session));
    }

}
