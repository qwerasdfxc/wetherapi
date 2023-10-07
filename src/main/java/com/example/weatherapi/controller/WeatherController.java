package com.example.weatherapi.controller;

import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.service.StationsService;
import com.example.weatherapi.service.WeatherService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/")
public class WeatherController {

    private final StationsService stationsService;
    private final WeatherService weatherService;

    public WeatherController(StationsService stationsService, WeatherService weatherService) {
        this.stationsService = stationsService;
        this.weatherService = weatherService;
    }

    @GetMapping("stations")
    public Flux<Station> getStations(){
        return stationsService.findAll();
    }

    @GetMapping("stations/{station_code}")
    public Mono<Weather> getWeatherByStation(@PathVariable Long station_code){
        return weatherService.findByStationId(station_code);
    }


    @PostMapping("save_stations")
    public String save() throws InterruptedException {
        stationsService.generateNewWeatherData();
        return "ok";
    }
}
