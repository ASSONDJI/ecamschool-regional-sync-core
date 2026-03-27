package com.ecamschool.regional.mapper;

import com.ecamschool.regional.dto.EducationalEstablishmentDTO;
import com.ecamschool.regional.dto.EducationalEstablishmentRequestDTO;
import com.ecamschool.regional.entity.EducationalEstablishment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateMapper.class)
public interface EducationalEstablishmentMapper {

    EducationalEstablishmentDTO toDto(EducationalEstablishment entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "dataMatrices", ignore = true)
    EducationalEstablishment toEntity(EducationalEstablishmentRequestDTO request);
}