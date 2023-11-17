package com.example.weatherapi.controller;

import com.example.weatherapi.DTO.UserDTO;import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.model.CloudType;
import com.example.weatherapi.model.Direction;
import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.security.BearerTokenServerAuthenticationConverter;
import com.example.weatherapi.security.JwtHandler;
import com.example.weatherapi.security.SecurityService;
import com.example.weatherapi.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Random;

@RestController
@RequestMapping("api/")
public class AuthController {

    private final UserService userService;

    private final SecurityService securityService;

    private final UserMapper userMapper;

    @Value("${jwt.secret}")
    private String secret;

    private final ReactiveRedisTemplate<String, Weather> weatherRedisTemplate;

    private final BearerTokenServerAuthenticationConverter bearerTokenServerAuthenticationConverter = new BearerTokenServerAuthenticationConverter(new JwtHandler(secret));

    public AuthController(UserService userService, SecurityService securityService, UserMapper userMapper, ReactiveRedisTemplate<String, Weather> weatherRedisTemplate) {
        this.userService = userService;
        this.securityService = securityService;
        this.userMapper = userMapper;
        this.weatherRedisTemplate = weatherRedisTemplate;
    }

    @PostMapping("register")
    public Mono<String> register(ServerWebExchange swe,
                                 @RequestBody UserDTO userDTO) {
        return userService.saveIfNotExists(userDTO)
                .then(securityService.authenticate(userDTO.getLogin(), userDTO.getPassword())
                        .flatMap(tokenDetails -> Mono.just(tokenDetails.getToken()))
        ).onErrorReturn("User already exists");
    }

    @PostMapping("get-api-key")
    public Mono<String> getKey(ServerWebExchange swe){
        String token = swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String username = (String) new JSONObject(new String( Base64.getDecoder().decode(token.split("\\.")[1]))).get("username");
        return userService.findUserByLogin(username).map(UserDTO::getKey);
    }


}
