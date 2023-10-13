package com.example.weatherapi.mapper;

import com.example.weatherapi.DTO.StationDTO;
import com.example.weatherapi.model.Station;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface StationMapper {
    StationDTO map(Station station);
    Station map(StationDTO dto);


}
