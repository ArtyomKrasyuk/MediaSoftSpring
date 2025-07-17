package com.example.MediaSoftSpring.mapstruct;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.dto.VisitorResponseDTO;
import com.example.MediaSoftSpring.entities.Visitor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitorMapper {
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE)")
    Visitor toEntity(VisitorRequestDTO dto);
    VisitorResponseDTO toDTO(Visitor visitor);
}
