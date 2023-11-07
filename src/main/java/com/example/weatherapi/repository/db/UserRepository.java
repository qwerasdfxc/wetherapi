package com.example.weatherapi.repository.db;

import com.example.weatherapi.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findFirstByLogin(String username);

    Mono<Boolean> existsByLogin(String username);

    Mono<Boolean> existsByKey(String key);

    Mono<User> findByKey(String key);

}
