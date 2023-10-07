package com.example.weatherapi.service;

import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.WeatherRepository;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Mono<Weather> findByStationId(Long stationId){
        return weatherRepository.findFirstByStationId(stationId);
    }
}
