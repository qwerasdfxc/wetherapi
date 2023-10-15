package com.example.weatherapi.mapper;

import com.example.weatherapi.DTO.StationDTO;
import com.example.weatherapi.DTO.WeatherDTO;
import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    WeatherDTO map(Weather weather);

    Weather map(WeatherDTO weatherDTO);


}
