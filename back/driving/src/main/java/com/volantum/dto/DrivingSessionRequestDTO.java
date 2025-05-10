package com.volantum.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DrivingSessionRequestDTO {
    private float distance;
    private List<EventRequestDTO> events;
    private int userId;
    private int carId; 
}
