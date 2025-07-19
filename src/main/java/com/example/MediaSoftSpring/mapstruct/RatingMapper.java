package com.example.MediaSoftSpring.mapstruct;

import com.example.MediaSoftSpring.dto.RatingPostRequestDTO;
import com.example.MediaSoftSpring.dto.RatingPutRequestDTO;
import com.example.MediaSoftSpring.dto.RatingResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.RatingId;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import com.example.MediaSoftSpring.repositories.VisitorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RatingMapper {
    @Autowired
    protected VisitorRepository visitorRepository;
    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Mapping(target = "id", expression = "java(new RatingId(dto.visitorId(), dto.restaurantId()))")
    @Mapping(target = "visitor", expression = "java(visitorRepository.findById(dto.visitorId()).orElseThrow())")
    @Mapping(target = "restaurant", expression = "java(restaurantRepository.findById(dto.restaurantId()).orElseThrow())")
    public abstract Rating toEntity(RatingPostRequestDTO dto);
    @Mapping(source = "id.visitorId", target = "visitorId")
    @Mapping(source = "id.restaurantId", target = "restaurantId")
    public abstract RatingResponseDTO toDTO(Rating rating);
    @Mapping(target = "id", ignore = true)
    public abstract void update(RatingPutRequestDTO dto, @MappingTarget Rating rating);

}
