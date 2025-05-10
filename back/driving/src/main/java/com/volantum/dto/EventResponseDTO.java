package com.volantum.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO {
    private int id;
    private LocalDateTime timestamp;
    private float latitude;
    private float longitude;
    private EventTypeResponseDTO type;
}
