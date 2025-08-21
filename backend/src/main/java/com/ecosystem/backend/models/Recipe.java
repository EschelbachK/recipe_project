package com.ecosystem.backend.models;

import java.util.List;

public record Recipe(
        String id,
        String title,
        String description,
        List<Ingredient> ingredients,
        List<String> instructions
) {}
