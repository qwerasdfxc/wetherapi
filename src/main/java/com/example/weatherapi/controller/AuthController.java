package com.example.weatherapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("api/")
public class AuthController {


    @PostMapping("register")
    public Mono<String> registration(Mono<Principal> principal){
        return principal.map(Principal::getName).map(name -> String.format("test: %s", name));
    }
}
