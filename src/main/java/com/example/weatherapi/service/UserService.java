package com.example.weatherapi.service;

import com.example.weatherapi.model.User;
import com.example.weatherapi.repository.UserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username).cast(UserDetails.class);
    }

    public Mono<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<User> save(User user){
        return userRepository.save(user);
    }
}
