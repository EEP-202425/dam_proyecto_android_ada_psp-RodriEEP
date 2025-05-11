package com.volantum.service;

import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import com.volantum.domain.*;
import com.volantum.dto.*;

@Service
public class DTOConverterService {
    
    public UserResponseDTO convertUserToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getScore(),
            user.getCars().stream().map(this::convertCarToDTO).collect(Collectors.toList()),
            user.getDrivingSessions().stream().map(this::convertDrivingSessionToDTO).collect(Collectors.toList())
        );

        if (user.getCars() != null) {
            dto.setCars(user.getCars().stream()
                .map(this::convertCarToDTO)
                .collect(Collectors.toList()));
        }

        if (user.getDrivingSessions() != null) {
            dto.setDrivingSessions(user.getDrivingSessions().stream()
                .sorted((s1, s2) -> s2.getStartTime().compareTo(s1.getStartTime()))
                .limit(2)
                .map(this::convertDrivingSessionToDTO)
                .collect(Collectors.toList()));
        }

        return dto;
    }

    public UserBasicResponseDTO convertUserToBasicDTO(User user) {
        return new UserBasicResponseDTO(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getScore()
        );
    }

    public CarResponseDTO convertCarToDTO(Car car) {
        return new CarResponseDTO(
            car.getId(),
            car.getPlate(),
            car.getBrand(),
            car.getModel(),
            car.getYearModel(),
            car.getImage(),
            car.getMileage()
        );
    }

    public DrivingSessionResponseDTO convertDrivingSessionToDTO(DrivingSession session) {
        return new DrivingSessionResponseDTO(
            session.getId(),
            session.getStartTime(),
            session.getEndTime(),
            session.getDistance(),
            session.getScore(),
            convertUserToBasicDTO(session.getUser()),
            convertCarToDTO(session.getCar()),
            session.getEvents().stream().map(this::convertEventToDTO).collect(Collectors.toList())
        );
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