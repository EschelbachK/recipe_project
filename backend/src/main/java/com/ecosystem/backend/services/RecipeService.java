package com.ecosystem.backend.services;

import com.ecosystem.backend.dto.RecipeDTO;
import com.ecosystem.backend.exception.RecipeNotFoundException;
import com.ecosystem.backend.models.Ingredient;
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

        return repository.save(new Recipe(
                idService.generateId(),
                dto.name(),
                dto.servings(),
                addMissingIngredientIds(dto.ingredients()),
                dto.description()
        ));
    }

    public Recipe updateRecipe(String id, RecipeDTO dto) {
        if (!repository.existsById(id)) {
            throw new RecipeNotFoundException("Recipe not found with id " + id);
        }

        return repository.save(new Recipe(
                id,
                dto.name(),
                dto.servings(),
                addMissingIngredientIds(dto.ingredients()),
                dto.description()
        ));
    }

    public void deleteRecipe(String id) {
        if (!repository.existsById(id)) {
            throw new RecipeNotFoundException("Recipe not found with id " + id);
        }
        repository.deleteById(id);
    }

    private List<Ingredient> addMissingIngredientIds(List<Ingredient> ingredients) {
        return ingredients.stream()
                .map(ing -> (ing.id() == null || ing.id().isBlank())
                        ? new Ingredient(idService.generateId(), ing.name(), ing.amount(), ing.unit())
                        : ing
                )
                .toList();
    }
}
