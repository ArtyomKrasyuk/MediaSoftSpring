package com.example.MediaSoftSpring.mapstruct;

import com.example.MediaSoftSpring.dto.RatingRequestDTO;
import com.example.MediaSoftSpring.dto.RatingResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE)")
    Rating toEntity(RatingRequestDTO dto);
    RatingResponseDTO toDTO(Rating rating);
}
