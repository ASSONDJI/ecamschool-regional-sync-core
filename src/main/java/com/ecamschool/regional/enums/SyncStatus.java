package com.ecamschool.regional.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SyncStatus {
    PENDING("PENDING"),
    SYNCED("SYNCED"),
    FAILED("FAILED");

    private final String value;

    SyncStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SyncStatus fromValue(String value) {
        if (value == null) return null;
        String cleanValue = value.replaceAll("^\"+|\"+$", "").trim();
        for (SyncStatus status : SyncStatus.values()) {
            if (status.value.equalsIgnoreCase(cleanValue)) {
                return status;
            }
        }
        return PENDING;
    }

    @Override
    public String toString() {
        return value;
    }
}