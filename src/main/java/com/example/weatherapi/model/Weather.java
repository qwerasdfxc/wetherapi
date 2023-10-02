package com.example.weatherapi.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table(schema = "weather", name = "weather")
@Builder

public class Weather {

    @Id
    @Column("id")
    private Long id;

    @Column("temperature")
    private Integer temperature;

    @Column("wind_kph")
    private Integer windKph;

    @Column("wind_dir")
    private Direction windDir;

    @Column("is_rain")
    private Boolean isRain;

    @Column("is_snow")
    private Boolean isSnow;

    @Column("octane")
    private Integer octane;

    @Column("cloud_type")
    private CloudType cloudType;

    @Column("station_id")
    private Long stationId;
}
