package com.example.weatherapi.mapper;

import com.example.weatherapi.DTO.StationDTO;
import com.example.weatherapi.model.Station;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    StationDTO toDto(Station model);

    Station toModel(StationDTO dto);
}
