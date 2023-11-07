package com.example.weatherapi.repository.db;

import com.example.weatherapi.model.Weather;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface WeatherRepository extends ReactiveCrudRepository<Weather, Long> {

    Mono<Weather> findFirstByStationId(Long stationId);
}
