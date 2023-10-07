package com.example.weatherapi.mapper;

import com.example.weatherapi.DTO.StationDTO;
import com.example.weatherapi.model.Station;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface StationMapper {
    StationDTO toDTO(Station station);

    Station toModel(StationDTO dto);
}
