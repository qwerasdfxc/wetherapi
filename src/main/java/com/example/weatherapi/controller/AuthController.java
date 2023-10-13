package com.example.weatherapi.controller;

import com.example.weatherapi.DTO.UserDTO;
import com.example.weatherapi.exception.AuthException;
import com.example.weatherapi.mapper.UserMapper;
import com.example.weatherapi.model.User;
import com.example.weatherapi.security.SecurityService;
import com.example.weatherapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final SecurityService securityService;

    private final UserMapper userMapper;


    @PostMapping("register")
    public Mono<String> register(ServerWebExchange swe,
                                  @RequestParam("login") String username,
                                  @RequestParam("password") String password) {
        User user = new User(username, password);
        return userService.saveIfNotExists(user)
                .then(securityService.authenticate(username, password)
                        .flatMap(tokenDetails -> Mono.just(tokenDetails.getToken()))
        ).onErrorReturn("User already exists");

//                .flatMap(x -> {
//                    ServerResponse.ok()
//                            .bodyValue(x);
//
//                }

//        return securityService.authenticate()
//        String key = jwtUtil.generateKey();
//        User user = User.builder().username(username).password(password).key(key).build();
//        userService.save(user);
//
//        return Mono.just(ResponseEntity.ok(jwtUtil.generateJWT(user)));
    }



//    private final JwtUtil jwtUtil;

//    public AuthController(UserService userService, JwtUtil jwtUtil) {
//        this.userService = userService;
//        this.jwtUtil = jwtUtil;
//    }


//    @PostMapping("register")
//    public Mono<ResponseEntity> register(ServerWebExchange swe,
//                                      @RequestParam("login") String username,
//                                      @RequestParam("password") String password) {
//        String key = jwtUtil.generateKey();
//        User user = User.builder().username(username).password(password).key(key).build();
//        userService.save(user);
//
//        return Mono.just(ResponseEntity.ok(jwtUtil.generateJWT(user)));
//    }
//
    @PostMapping("get-api-key")
    public Mono<ResponseEntity<?>> getKey(ServerWebExchange swe,
                                          @RequestParam("login") String login,
                                          @RequestParam("password") String password){
//        return Mono.just(ResponseEntity.ok(jwtUtil.generateKey()));
        return userService.findUserByUsername(login).map(x ->
                    x.getPassword().equals(password) ?
                            ResponseEntity.ok(x.getKey())
                            : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
                ).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        );
    }
}
