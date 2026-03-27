package com.ecamschool.regional.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public class DateMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atOffset(ZoneOffset.UTC);
    }

    public LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.toLocalDateTime();
    }


    public String mapDataValueToString(Object dataValue) {
        if (dataValue == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(dataValue);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize dataValue to JSON", e);
        }
    }


    public Object mapStringToDataValue(String dataValue) {
        if (dataValue == null) {
            return null;
        }
        try {
            return objectMapper.readValue(dataValue, Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize dataValue from JSON", e);
        }
    }
}