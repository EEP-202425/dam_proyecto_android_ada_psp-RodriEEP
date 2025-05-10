package com.volantum.dto;

import com.volantum.enums.EventSeverity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventTypeResponseDTO {
    private int id;
    private String name;
    private String description;
    private EventSeverity severity;
}
