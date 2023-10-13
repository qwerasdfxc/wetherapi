package com.example.weatherapi.repository;

import com.example.weatherapi.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findFirstByUsername(String username);

    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByKey(String key);

}
