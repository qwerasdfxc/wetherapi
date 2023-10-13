package com.example.weatherapi.controller;

import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.service.StationsService;
import com.example.weatherapi.service.UserService;
import com.example.weatherapi.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class WeatherController {

    private final StationsService stationsService;
    private final UserService userService;
    private final WeatherService weatherService;


    @GetMapping("stations")
    public Flux<?> getStations(ServerWebExchange serverWebExchange, @RequestParam("key") String key){
        return userService.existsByKey(key).flatMapMany(x -> {
            if(x)
                return stationsService.findAll();
            else{
                serverWebExchange.getResponse().setStatusCode(HttpStatusCode.valueOf(400));
                return Flux.just("Key isn't valid");
            }
        });
    }

    @GetMapping("stations/{station_code}")
    public Mono<?> getWeatherByStation(ServerWebExchange serverWebExchange, @PathVariable Long station_code, @RequestParam("key") String key){
        return userService.existsByKey(key).flatMap(x -> {
            if(x)
                return weatherService.findByStationId(station_code);
            else{
                serverWebExchange.getResponse().setStatusCode(HttpStatusCode.valueOf(400));
                return Mono.just("Key isn't valid");
            }
        });

    }


    @PostMapping("save_stations")
    public String save() throws InterruptedException {
        stationsService.generateNewWeatherData();
        return "ok";
    }
}
