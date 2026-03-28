package com.ecamschool.regional.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DataType {
    TEACHER_NEED("TEACHER_NEED"),
    SCHOOL_STATS("SCHOOL_STATS"),
    TEACHER_BALANCE("TEACHER_BALANCE");

    private final String value;

    DataType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static DataType fromValue(String value) {
        if (value == null) return null;
        String cleanValue = value.replaceAll("^\"+|\"+$", "").trim();
        for (DataType type : DataType.values()) {
            if (type.value.equalsIgnoreCase(cleanValue)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}