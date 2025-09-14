package com.ecosystem.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record ShoppingListAddRequest(
        String recipeId,
        List<IngredientPortion> items
) {
    public record IngredientPortion(
            String ingredientId,
            String name,
            BigDecimal amount,
            String unit
    ) {}
}