package com.ecamschool.regional.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    ADMIN("ADMIN"),
    DELEGUE("DELEGUE"),
    CHEF_ETABLISSEMENT("CHEF_ETABLISSEMENT");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserRole fromValue(String value) {
        if (value == null) return null;
        String cleanValue = value.replaceAll("^\"+|\"+$", "").trim();
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(cleanValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + value);
    }

    @Override
    public String toString() {
        return value;
    }
}