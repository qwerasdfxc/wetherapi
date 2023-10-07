package com.example.weatherapi.DTO;

import com.example.weatherapi.model.CloudType;
import com.example.weatherapi.model.Direction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherDTO {

    @JsonIgnore
    private Long id;

    private Integer temperature;

    private Integer windKph;

    private Direction windDir;

    private Boolean isRain;

    private Boolean isSnow;

    private Integer octane;

    private CloudType cloudType;

    private Long stationId;
}
