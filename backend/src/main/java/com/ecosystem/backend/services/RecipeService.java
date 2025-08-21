package com.ecosystem.backend.services;

import com.ecosystem.backend.dto.RecipeDTO;
import com.ecosystem.backend.exception.RecipeNotFoundException;
import com.ecosystem.backend.models.Recipe;
import com.ecosystem.backend.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository repository;
    private final com.ecosystem.backend.services.IdService idService;

    public RecipeService(RecipeRepository repository, com.ecosystem.backend.services.IdService idService) {
        this.repository = repository;
        this.idService = idService;
    }

    public List<Recipe> getAllRecipes() {
        return repository.findAll();
    }

    public Recipe getRecipeById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id " + id));
    }

    public Recipe createRecipe(RecipeDTO dto) {
        Recipe recipe = new Recipe(
                idService.generateId(),
                dto.title(),
                dto.description(),
                dto.ingredients(),
                dto.instructions()
        );
        return repository.save(recipe);
    }

    public Recipe updateRecipe(String id, RecipeDTO dto) {
        Recipe existing = repository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe not found with id " + id));

        Recipe updated = new Recipe(
                existing.id(),
                dto.title(),
                dto.description(),
                dto.ingredients(),
                dto.instructions()
        );
        return repository.save(updated);
    }

    public void deleteRecipe(String id) {
        repository.deleteById(id);
    }
}
