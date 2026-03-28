package com.ecamschool.regional.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public class DateMapper {

    public OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return localDateTime.atOffset(ZoneOffset.UTC);
    }

    public LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) return null;
        return offsetDateTime.toLocalDateTime();
    }


    public Object mapStringToDataValue(String dataValue) {
        if (dataValue == null) return null;

        return dataValue;
    }

    public String mapDataValueToString(Object dataValue) {
        if (dataValue == null) return null;
        return dataValue.toString();
    }
}