package com.example.weatherapi.service;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface UserDetailsService {
    Mono<UserDetails> findByUsername(String username);
}
