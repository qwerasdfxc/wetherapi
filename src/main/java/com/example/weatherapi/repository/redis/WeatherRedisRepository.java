package com.example.weatherapi.repository.redis;

import com.example.weatherapi.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WeatherRedisRepository {
    private final ReactiveValueOperations<String, Weather> weatherReactiveValueOperations;

    private final ReactiveRedisTemplate<String, Weather> redisTemplate;


    public Mono<Boolean> save(Weather weather){
        return weatherReactiveValueOperations.set(genarateKey(weather.getStationId()), weather);

    }

    private String genarateKey(Long stationId) {

        return null;
    }

}
