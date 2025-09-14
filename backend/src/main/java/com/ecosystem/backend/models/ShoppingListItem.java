package com.ecosystem.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "shopping_list_items")
public record ShoppingListItem(
        @Id String id,
        String userId,
        String recipeId,
        String ingredientId,
        String name,
        BigDecimal amount,
        String unit,
        boolean done
) {}