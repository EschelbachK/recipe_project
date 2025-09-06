package com.ecosystem.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "favorites")
public record Favorites(@Id String id, @Indexed(unique = true) String userId, List<String> recipeIds) {

    public Favorites {
        if (recipeIds == null) {
            recipeIds = new ArrayList<>();
        }
    }
}