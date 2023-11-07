package com.example.weatherapi.service;

import com.example.weatherapi.DTO.StationDTO;
import com.example.weatherapi.mapper.StationListMapper;
import com.example.weatherapi.mapper.StationMapper;
import com.example.weatherapi.model.Station;
import com.example.weatherapi.repository.db.StationRepository;
import com.example.weatherapi.repository.db.WeatherRepository;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Random;

@Service
public class StationsService {

    private final StationRepository stationRepository;
    private final WeatherRepository weatherRepository;

    private final StationMapper stationMapper;
    private final StationListMapper stationListMapper;
    private final Random random = new Random();

//    private final RedisTemplate<String, Station> stationRedisTemplate;

    private final ReactiveValueOperations<String, Station> stationReactiveValueOperations;


    public StationsService(StationRepository stationRepository, WeatherRepository weatherRepository, StationMapper stationMapper, StationListMapper stationListMapper, ReactiveValueOperations<String, Station> stationReactiveValueOperations) {
        this.stationRepository = stationRepository;
        this.weatherRepository = weatherRepository;
        this.stationMapper = stationMapper;
        this.stationListMapper = stationListMapper;
//        this.stationRedisTemplate = stationRedisTemplate;
        this.stationReactiveValueOperations = stationReactiveValueOperations;
    }



    public Flux<StationDTO> findAll() {
//        return stationReactiveValueOperations.multiGet(List.of("station*"))
        return stationRepository.findAll().map(stationMapper::map);
    }

}
