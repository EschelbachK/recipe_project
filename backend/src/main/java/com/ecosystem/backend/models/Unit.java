package com.ecosystem.backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Unit {
    KG("kg"),
    G("g"),
    ML("ml"),
    L("l"),
    PIECE("piece");

    private final String label;

    Unit(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
    @JsonCreator
    public static Unit from(String v) {
        for (Unit u : values())
            if (u.name().equalsIgnoreCase(v) || u.getLabel().equalsIgnoreCase(v)) return u;
        throw new IllegalArgumentException("Unknown unit: " + v);
    }
}