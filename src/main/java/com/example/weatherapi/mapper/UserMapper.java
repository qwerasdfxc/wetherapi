package com.example.weatherapi.mapper;

import com.example.weatherapi.DTO.UserDTO;
import com.example.weatherapi.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public UserDTO map(User user);

    @InheritInverseConfiguration
    public User map(UserDTO userDTO);
}
