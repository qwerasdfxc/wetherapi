package com.example.weatherapi.service;

import com.example.weatherapi.DTO.StationDTO;
import com.example.weatherapi.mapper.StationListMapper;
import com.example.weatherapi.mapper.StationMapper;
import com.example.weatherapi.model.CloudType;
import com.example.weatherapi.model.Direction;
import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import com.example.weatherapi.repository.StationRepository;
import com.example.weatherapi.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Random;

@Service
public class StationsService {

    private final StationRepository stationRepository;
    private final WeatherRepository weatherRepository;

    private final StationMapper stationMapper;
    private final StationListMapper stationListMapper;
    private final Random random = new Random();


    public StationsService(StationRepository stationRepository, WeatherRepository weatherRepository, StationMapper stationMapper, StationListMapper stationListMapper) {
        this.stationRepository = stationRepository;
        this.weatherRepository = weatherRepository;
        this.stationMapper = stationMapper;
        this.stationListMapper = stationListMapper;
    }

    //    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void generateNewWeatherData() {
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
                    return Mono.just(weather);
                }
        ).subscribe();


    }

    public Flux<StationDTO> findAll() {
        //TODO how to convert flux<Station> to DTO using mapstruct
//        return stationRepository.findAll().collectList().flatMap(x -> se)
        return stationRepository.findAll().map(stationMapper::map);
//        return stationRepository.findAll();

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
