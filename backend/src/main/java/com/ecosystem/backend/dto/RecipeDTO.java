package com.ecosystem.backend.dto;

import com.ecosystem.backend.models.Ingredient;
import java.util.List;

public record RecipeDTO(
        String title,
        String description,
        List<Ingredient> ingredients,
        List<String> instructions
) {}
