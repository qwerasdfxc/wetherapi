package com.example.weatherapi.service;

import com.example.weatherapi.model.CloudType;
import com.example.weatherapi.model.Direction;
import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.db.StationRepository;
import com.example.weatherapi.repository.db.WeatherRepository;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

@Service
public class DataGeneratorService {

    private final WeatherRepository weatherRepository;
    private final Random random = new Random();

    private final StationRepository stationRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH");

    private final ReactiveValueOperations<String, Weather> weatherRedisOperations;

    private final ReactiveValueOperations<String, Station> stationRedisOperations;

    public DataGeneratorService(WeatherRepository weatherRepository, StationRepository stationRepository, ReactiveValueOperations<String, Weather> weatherRedisOperations, ReactiveValueOperations<String, Station> stationRedisOperations) {
        this.weatherRepository = weatherRepository;
        this.weatherRedisOperations = weatherRedisOperations;
        this.stationRedisOperations = stationRedisOperations;
        this.stationRepository = stationRepository;

    }

    @Scheduled(cron = "0 */3 * * *")
    public Mono<String> generateNewWeatherData() {
        weatherRepository.findAll().flatMap(x ->
                {
                    Weather weather = Weather.builder()
                            .id(x.getId())
                            .stationId(x.getStationId())
                            .octane((int) (Math.random() * 10))
                            .cloudType(CloudType.values()[random.nextInt(CloudType.values().length)])
                            .windKph((int) (Math.random() * 30))
                            .temperature((int) (Math.random() * 70) - 35)
                            .isSnow(random.nextBoolean())
                            .isRain(random.nextBoolean())
                            .windDir(Direction.values()[random.nextInt(Direction.values().length)])
                            .build();
                    weatherRepository.save(weather).subscribe();
                    weatherRedisOperations.set("weather#"+weather.getStationId()+"#"+ LocalDateTime.now().format(formatter), weather).subscribe();
                    return Mono.just(weather);
                }
        ).subscribe();

        return Mono.just("ok");
    }

    public void saveRandomStations() {
        Locale[] locales = Locale.getAvailableLocales();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Station station = Station.builder()
                    .country(locales[i].getCountry())
                    .code(random.ints(97, 122).limit(3).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString())
                    .build();
            stationRedisOperations.set("station"+station.getId(), station);
            stationRepository.save(station).subscribe();
        }
    }

}
