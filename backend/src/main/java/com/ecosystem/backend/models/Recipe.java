package com.ecosystem.backend.models;

import java.util.List;

public record Recipe(
        String id,
        String name,
        Double servings,
        List<Ingredient> ingredients,
        String description
) {
}
