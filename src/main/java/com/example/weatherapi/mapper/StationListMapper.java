package com.example.weatherapi.mapper;

import com.example.weatherapi.DTO.StationDTO;
import com.example.weatherapi.model.Station;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = StationMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StationListMapper {
    List<Station> toModel(List<StationDTO> stationDTOS);
    List<StationDTO> toDTO(List<Station> stationList);
}
