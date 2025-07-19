package com.example.MediaSoftSpring.mapstruct;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.dto.RestaurantResponseDTO;
import com.example.MediaSoftSpring.entities.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    Restaurant toEntity(RestaurantRequestDTO dto);
    RestaurantResponseDTO toDTO(Restaurant restaurant);
    @Mapping(target = "id", ignore = true)
    void update(RestaurantRequestDTO dto, @MappingTarget Restaurant restaurant);
}
