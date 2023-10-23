package com.example.weatherapi.controller;

import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.service.StationsService;
import com.example.weatherapi.service.UserService;
import com.example.weatherapi.service.WeatherService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("api/")
public class WeatherController {

    private final StationsService stationsService;
    private final UserService userService;
    private final WeatherService weatherService;
    private final Bucket bucket;

    public WeatherController(StationsService stationsService, UserService userService, WeatherService weatherService) {
        this.stationsService = stationsService;
        this.userService = userService;
        this.weatherService = weatherService;
        Bandwidth bandwidth = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(bandwidth)
                .build();
    }

    @GetMapping("stations")
    public Flux<?> getStations(ServerWebExchange serverWebExchange) {
        if (bucket.tryConsume(1)) {

            return userService.existsByKey(serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION)).flatMapMany(x -> {
                if (x)
                    return stationsService.findAll();
                else {
                    serverWebExchange.getResponse().setStatusCode(HttpStatusCode.valueOf(400));
                    return Flux.just("Key isn't valid");
                }
            });
        } else {
            serverWebExchange.getResponse().setStatusCode(HttpStatusCode.valueOf(429));
            return Flux.empty();
        }
    }

    @GetMapping("stations/{station_code}")
    public Mono<?> getWeatherByStation(ServerWebExchange serverWebExchange, @PathVariable Long station_code) {
        if (bucket.tryConsume(1)) {


            return userService.existsByKey(serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION)).flatMap(x -> {
                if (x)
                    return weatherService.findByStationId(station_code);
                else {
                    serverWebExchange.getResponse().setStatusCode(HttpStatusCode.valueOf(400));
                    return Mono.just("Key isn't valid");
                }
            });
        } else {
            serverWebExchange.getResponse().setStatusCode(HttpStatusCode.valueOf(429));
            return Mono.empty();
        }

    }


    @PostMapping("save_stations")
    public String save() throws InterruptedException {
        stationsService.generateNewWeatherData();
        return "ok";
    }
}
