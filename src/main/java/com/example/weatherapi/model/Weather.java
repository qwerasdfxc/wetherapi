package com.example.weatherapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@Table(schema = "weather", name = "weather") @Builder
@RequiredArgsConstructor
@AllArgsConstructor
//@RedisHash("Weather")
public class Weather implements Serializable {

    @Id
    @Column("id")
    @JsonIgnore
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
