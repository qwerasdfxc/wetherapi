package com.example.weatherapi.service;

import com.example.weatherapi.model.CloudType;
import com.example.weatherapi.model.Direction;
import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.StationRepository;
import com.example.weatherapi.repository.WeatherRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class StationsService {
    Logger logger = Logger.getLogger("StationsService");

    private final StationRepository stationRepository;
    private final WeatherRepository weatherRepository;


    public StationsService(StationRepository stationRepository, WeatherRepository weatherRepository) {
        this.stationRepository = stationRepository;
        this.weatherRepository = weatherRepository;
    }

//    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void generateNewWeatherData(){
        Random random = new Random();
        Flux<Weather> weatherFlux = weatherRepository.findAll().flatMap(x ->
                {
                    Weather weather = Weather.builder()
                            .id(x.getId())
                            .stationId(x.getStationId())
                            .octane((int) (Math.random() * 10))
                            .cloudType(CloudType.values()[random.nextInt(CloudType.values().length)])
                            .windKph((int) (Math.random() * 30))
                            .temperature((int)( Math.random() * 70) - 35)
                            .isSnow(random.nextBoolean())
                            .isRain(random.nextBoolean())
                            .windDir(Direction.values()[random.nextInt(Direction.values().length)])
                            .build();
                    weatherRepository.save(weather).subscribe();
                    return Mono.just(weather);
                }
        );

        weatherFlux.subscribe();

    }

    public Flux<Station> findAll(){
        return stationRepository.findAll();
    }

    public void saveRandomStations() {
        Locale[] locales = Locale.getAvailableLocales();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Station station = Station.builder()
                    .country(locales[i].getCountry())
                    .code(random.ints(97, 122).limit(3).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString())
                    .build();
            stationRepository.save(station).subscribe();
        }
    }


}
