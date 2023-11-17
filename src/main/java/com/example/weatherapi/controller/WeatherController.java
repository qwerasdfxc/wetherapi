package com.example.weatherapi.controller;

import com.example.weatherapi.service.DataGeneratorService;
import com.example.weatherapi.service.StationsService;
import com.example.weatherapi.service.UserService;
import com.example.weatherapi.service.WeatherService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("api/")
public class WeatherController {

    private final StationsService stationsService;
    private final WeatherService weatherService;

    private final DataGeneratorService dataGeneratorService;

    public WeatherController(StationsService stationsService, WeatherService weatherService, DataGeneratorService dataGeneratorService) {
        this.stationsService = stationsService;
        this.weatherService = weatherService;
        this.dataGeneratorService = dataGeneratorService;

    }

    @GetMapping("stations")
    public Flux<?> getStations(ServerWebExchange serverWebExchange) {
        return stationsService.findAll();
    }

    @GetMapping("stations/{station_code}")
    public Mono<?> getWeatherByStation(ServerWebExchange serverWebExchange, @PathVariable Long station_code) {
        return weatherService.findByStationId(station_code);
    }


    @PostMapping("save_stations")
    public Mono<?> save(ServerWebExchange serverWebExchange) throws InterruptedException {
        return dataGeneratorService.generateNewWeatherData();

    }
}
