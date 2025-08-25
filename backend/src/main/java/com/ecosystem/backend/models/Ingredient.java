package com.ecosystem.backend.models;

import org.springframework.data.mongodb.core.mapping.Field;

public record Ingredient(
        String id,
        String name,
        double amount,
        @Field("unit") Unit unit
) {
}