package com.ecosystem.backend.models;

public enum Unit {
    KG("kg"), G("g"), ML("ml"), L("l"), PIECE("piece");

    public final String label;

    Unit(String label) {
        this.label = label;
    }
}
