package com.ecosystem.backend.repository;

import com.ecosystem.backend.models.ShoppingListItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends MongoRepository<ShoppingListItem, String> {
    List<ShoppingListItem> findAllByUserId(String userId);
    Optional<ShoppingListItem> findByIdAndUserId(String id, String userId);
    List<ShoppingListItem> findAllByUserIdAndRecipeId(String userId, String recipeId);
}