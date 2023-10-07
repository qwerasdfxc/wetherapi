package com.example.weatherapi.DTO;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
public class StationDTO {

    private Long id;

    private String code;

    private String country;
}
