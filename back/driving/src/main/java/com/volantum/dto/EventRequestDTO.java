package com.volantum.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDTO {
	private LocalDateTime timestamp;
    private float latitude;
    private float longitude;
    private int eventTypeId;
}
