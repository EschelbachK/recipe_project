package com.ecosystem.backend.repository;

import com.ecosystem.backend.models.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipeRepository extends MongoRepository<Recipe, String> {}
