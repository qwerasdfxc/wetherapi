package com.example.weatherapi.service;

import com.example.weatherapi.DTO.WeatherDTO;
import com.example.weatherapi.mapper.WeatherMapper;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherMapper weatherMapper;

    private final WeatherRepository weatherRepository;



    public Mono<WeatherDTO> findByStationId(Long stationId){
        return weatherRepository.findFirstByStationId(stationId).map(station -> weatherMapper.map(station));
    }
}
