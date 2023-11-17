package com.example.weatherapi.repository.redis;

import com.example.weatherapi.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class WeatherRedisRepository {
    private final ReactiveValueOperations<String, Weather> weatherReactiveValueOperations;

    private final ReactiveRedisTemplate<String, Weather> redisTemplate;

    @Value("${dateTimePattern}")
    private String DatePatternString;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH");


    public Mono<Boolean> save(Weather weather){
        return weatherReactiveValueOperations.set(generateRedisWeatherKey(weather.getStationId()), weather);

    }

    public Mono<Weather> getLastById(Long stationId){
        return weatherReactiveValueOperations.get(generateRedisWeatherKey(stationId));
    }

//    private String genarateKey(Long stationId) {
//
//        return null;
//    }

    private String generateRedisWeatherKey(long id){
        //hours == 0, 3, 6, 9...
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(LocalDateTime.now().getHour()%3);
        return  "weather#"+id+"#"+localDateTime.format(formatter);
    }

}
