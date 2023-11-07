package com.example.weatherapi.service;

import com.example.weatherapi.DTO.WeatherDTO;
import com.example.weatherapi.mapper.WeatherMapper;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.db.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WeatherMapper weatherMapper;

    private final WeatherRepository weatherRepository;



    //в конфиг
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH");


    public Mono<WeatherDTO> findByStationId(Long stationId) {
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(3);
        Weather weather1 = new Weather();
        return redisTemplate
                //weather#12#13.12.2001
                .keys("weather" + stationId + "*").
                flatMap(key -> {
                    //четкий индекс
                    //weather redis repository
                            if (LocalDateTime.parse(key.substring(key.lastIndexOf("#") + 1), formatter).isAfter(localDateTime)) {
                                return weatherReactiveValueOperations.get(key);
                            }
                            return Flux.just(new Weather());
                        }
                )
                .filter(weather -> weather.getId() != null)
                .map(weatherMapper::map)
                .next()
                .switchIfEmpty(getByIdFromDB(stationId);  );
        //TODO генерация ключа в отдельный метод
    }

    private String generateRedisWeatherKey(String id){
        return null;
    }

    public Mono<WeatherDTO> getByIdFromDB(Long stationId) {
        //TODO get last
        //log info
        System.out.println("data from db");
        return weatherRepository.findFirstByStationId(stationId).map(station -> weatherMapper.map(station));

    }
}
