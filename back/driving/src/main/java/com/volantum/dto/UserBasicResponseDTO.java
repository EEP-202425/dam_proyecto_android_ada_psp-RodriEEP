package com.volantum.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasicResponseDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private float score;
}