package com.example.weatherapi.repository.redis;

import com.example.weatherapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRedisRepository {

    private final ReactiveValueOperations<String, User> userReactiveValueOperations;

    private final ReactiveRedisTemplate<String, User> userReactiveRedisTemplate;

    public Mono<Boolean> saveUser(User user){
        return userReactiveValueOperations.set(user.getKey(), user);
    }

    public Mono<Boolean> existsByKey(String key){
        return userReactiveRedisTemplate.hasKey(key);
    }
}
