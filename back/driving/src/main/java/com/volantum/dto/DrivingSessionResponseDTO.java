package com.volantum.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingSessionResponseDTO {
	private int id;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private float distance;
    private String duration;
    private float averageSpeed;
    private float score;
    private UserBasicResponseDTO user;
    private CarResponseDTO car;
    private List<EventResponseDTO> events;
}
