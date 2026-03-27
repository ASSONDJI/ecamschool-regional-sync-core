package com.ecamschool.regional.mapper;

import com.ecamschool.regional.dto.DataMatrixDTO;
import com.ecamschool.regional.dto.DataMatrixRequestDTO;
import com.ecamschool.regional.entity.DataMatrix;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DateMapper.class, UserMapper.class, EducationalEstablishmentMapper.class})
public interface DataMatrixMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "establishment", target = "establishment")
    DataMatrixDTO toDto(DataMatrix entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "syncedAt", ignore = true)
    @Mapping(target = "syncStatus", constant = "PENDING")
    @Mapping(target = "version", constant = "1")
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "establishment", ignore = true)
    DataMatrix toEntity(DataMatrixRequestDTO request);
}