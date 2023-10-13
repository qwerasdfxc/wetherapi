package com.example.weatherapi.mapper;

import com.example.weatherapi.DTO.StationDTO;
import com.example.weatherapi.DTO.WeatherDTO;
import com.example.weatherapi.model.Station;
import com.example.weatherapi.model.Weather;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = WeatherMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface WeatherListMapper {

    List<Weather> toModel(List<WeatherDTO> weatherDTOS);
    List<WeatherDTO> toDTO(List<Weather> weatherList);
}
