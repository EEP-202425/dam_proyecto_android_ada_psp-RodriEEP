package com.volantum.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarRequestDTO {
    private String plate;
    private String brand;
    private String model;
    private int yearModel;
    private String image;
    private Double mileage;
}
