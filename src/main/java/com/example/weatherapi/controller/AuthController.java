package com.example.weatherapi.controller;

import com.example.weatherapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("register")
    public Mono<ResponseEntity> register(ServerWebExchange swe,
                                      @RequestParam("login") String username,
                                      @RequestParam("password") String password) {
        return
    }

    @PostMapping("get-api-key")
    public Mono<ResponseEntity<?>> getKey(ServerWebExchange swe,
                                          @RequestParam("login") String username,
                                          @RequestParam("password") String password){
        return userService.findUserByUsername(username).map(x ->
                    x.getPassword().equals(password) ?
                            ResponseEntity.ok(x.getKey())
                            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                ).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        );
    }


}
