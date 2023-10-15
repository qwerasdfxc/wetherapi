package com.example.weatherapi.controller;

import com.example.weatherapi.DTO.UserDTO;import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.security.BearerTokenServerAuthenticationConverter;
import com.example.weatherapi.security.JwtHandler;
import com.example.weatherapi.security.SecurityService;
import com.example.weatherapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final SecurityService securityService;

    private final UserMapper userMapper;

    @Value("${jwt.secret}")
    private String secret;

    private final BearerTokenServerAuthenticationConverter bearerTokenServerAuthenticationConverter = new BearerTokenServerAuthenticationConverter(new JwtHandler(secret));

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
        String payload = new String( Base64.getDecoder().decode(token.split("\\.")[1]));
        JSONObject jsonObject = new JSONObject(new String( Base64.getDecoder().decode(token.split("\\.")[1])));
        String username = (String) jsonObject.get("username");
        System.out.println("payload");
        System.out.println(payload);
        return userService.findUserByLogin(username).map(UserDTO::getKey);
//        return Mono.empty();
//        return userService.findUserByLogin(login).map(x ->
//                    x.getPassword().equals(password) ?
//                            ResponseEntity.ok(x.getKey())
//                            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
//                ).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
//        );
    }
}
