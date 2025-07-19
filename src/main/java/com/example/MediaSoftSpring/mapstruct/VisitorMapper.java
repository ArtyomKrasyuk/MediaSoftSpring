package com.example.MediaSoftSpring.mapstruct;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.dto.VisitorResponseDTO;
import com.example.MediaSoftSpring.entities.Visitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VisitorMapper {
    @Mapping(target = "id", ignore = true)
    Visitor toEntity(VisitorRequestDTO dto);
    VisitorResponseDTO toDTO(Visitor visitor);
    @Mapping(target = "id", ignore = true)
    void update(VisitorRequestDTO dto, @MappingTarget Visitor visitor);
}
