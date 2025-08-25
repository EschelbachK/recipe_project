package com.ecosystem.backend.models;

public record Ingredient(
        String id,
        String name,
        double amount,
        Unit unit
) {
}