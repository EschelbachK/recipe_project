
package com.ecosystem.backend.dto;

import com.ecosystem.backend.models.Ingredient;

import java.util.List;

public record RecipeDTO(
        String name,
        Double servings,
        List<Ingredient> ingredients,
        String description
) {
}
