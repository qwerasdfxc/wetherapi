package com.example.weatherapi.service;

import com.example.weatherapi.model.Station;
import com.example.weatherapi.repository.StationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class StationsService {
    Logger logger = Logger.getLogger("StationsService");

    private final StationRepository stationRepository;


    public StationsService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Scheduled(cron = "0 */3 * * *")
    public void getDataFromWeatherApi() {
//        WebClient.ResponseSpec responseSpec = webClient

    }

    public void randomStationsGenerator(){
        Locale[] locales = Locale.getAvailableLocales();
        Random random = new Random();

        for (int i = 0; i < 10; i++){
            Station station = Station.builder()
                    .country(locales[i].getCountry())
                    .code(random.ints(97, 122).limit(3).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString())
                    .build();
            stationRepository.save(station);
        }
    }


}
