package com.example.MediaSoftSpring.mapstruct;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.dto.RestaurantResponseDTO;
import com.example.MediaSoftSpring.entities.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE)")
    Restaurant toEntity(RestaurantRequestDTO dto);
    RestaurantResponseDTO toDTO(Restaurant restaurant);
}
