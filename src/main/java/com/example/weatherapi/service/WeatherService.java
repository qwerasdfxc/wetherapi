package com.example.weatherapi.service;

import com.example.weatherapi.DTO.WeatherDTO;
import com.example.weatherapi.mapper.WeatherMapper;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.db.WeatherRepository;
import com.example.weatherapi.repository.redis.WeatherRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WeatherMapper weatherMapper;

    private final WeatherRepository weatherRepository;

    private final WeatherRedisRepository weatherRedisRepository;

    private final ReactiveRedisTemplate<String, Weather> redisTemplate;

    private final ReactiveValueOperations<String, Weather> weatherReactiveValueOperations;


    //в конфиг
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH");


    public Mono<Weather> findByStationId(Long stationId) {

        return weatherRedisRepository.getLastById(stationId)
                .switchIfEmpty(findWeatherByStationIdAndSaveInRedis(stationId));
//        LocalDateTime localDateTime = LocalDateTime.now().minusHours(3);
//        return redisTemplate
//                //weather#12#13.12.2001
//                .keys("weather" + stationId + "*").
//                flatMap(key -> {
//                    //четкий индекс
//                    //weather redis repository
//                    weatherReactiveValueOperations.get(generateRedisWeatherKey(key))
//                            if (LocalDateTime.parse(key.substring(key.lastIndexOf("#") + 1), formatter).isAfter(localDateTime)) {
//                                return weatherReactiveValueOperations.get(key);
//                            }
//                            return Flux.just(new Weather());
//                        }
//                )
//                .filter(weather -> weather.getId() != null)
//                .map(weatherMapper::map)
//                .next()
//                .switchIfEmpty(getByIdFromDB(stationId)  );
        //TODO генерация ключа в отдельный метод
    }

    private Mono<Weather> findWeatherByStationIdAndSaveInRedis(Long stationId) {
        return weatherRepository.findFirstByStationId(stationId).map(weather -> {
            weatherRedisRepository.save(weather).subscribe();
            return weather;
        });

    }
}
