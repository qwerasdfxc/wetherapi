package com.example.weatherapi.controller;

import com.example.weatherapi.service.StationsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("api/")
public class AuthController {

    private final StationsService stationsService;

    public AuthController(StationsService stationsService) {
        this.stationsService = stationsService;
    }


    @PostMapping("register")
    public Mono<String> registration(Mono<Principal> principal) {
        stationsService.getDataFromWeatherApi();
        return principal
                .map(Principal::getName)
                .map(name -> String.format("Hello, %s", name));
    }

    @PostMapping("save_stations")
    public String savee(){
        stationsService.randomStationsGenerator();
        return "ok";
    }
}
