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
    private String temperature;

    @Column("wind_kph")
    private String windKph;

    @Column("wind_dir")
    private String windDir;

    @Column("will_it_rain")
    private Boolean isRain;

    @Column("will_it_snow")
    private Boolean isSnow;

    @Column("octane")
    private Integer octane;

    @Column("cloud_type")
    private String cloudType;

    @Column("station_id")
    private Long stationId;
}
