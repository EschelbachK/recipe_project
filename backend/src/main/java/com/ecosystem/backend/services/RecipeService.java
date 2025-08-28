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
    private final IdService idService;

    public RecipeService(RecipeRepository repository, IdService idService) {
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
                dto.name(),
                dto.servings(),
                dto.ingredients(),
                dto.description()
        );
        return repository.save(recipe);
    }

    public Recipe updateRecipe(String id, RecipeDTO dto) {
        if (!repository.existsById(id)) {
            throw new RecipeNotFoundException("Recipe not found with id " + id);
        }
        return repository.save(new Recipe(
                id,
                dto.name(),
                dto.servings(),
                dto.ingredients(),
                dto.description()
        ));
    }

    public void deleteRecipe(String id) {
        if (!repository.existsById(id)) {
            throw new RecipeNotFoundException("Recipe not found with id " + id);
        }
        repository.deleteById(id);
    }
}
