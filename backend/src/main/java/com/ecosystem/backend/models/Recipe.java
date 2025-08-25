package com.ecosystem.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")
public record Recipe(
        @Id String id,
        String name,
        Double servings,
        List<Ingredient> ingredients,
        String description
) {}
