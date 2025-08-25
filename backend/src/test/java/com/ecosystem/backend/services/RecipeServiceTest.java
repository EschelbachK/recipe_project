package com.ecosystem.backend.services;

import com.ecosystem.backend.dto.RecipeDTO;
import com.ecosystem.backend.exception.RecipeNotFoundException;
import com.ecosystem.backend.models.Ingredient;
import com.ecosystem.backend.models.Recipe;
import com.ecosystem.backend.models.Unit;
import com.ecosystem.backend.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    RecipeRepository repository = Mockito.mock(RecipeRepository.class);
    IdService idService = Mockito.mock(IdService.class);
    RecipeService service = new RecipeService(repository, idService);

    @Test
    void getAllRecipes() {
        Recipe r1 = new Recipe("1", "Pancakes", 4.0,
                List.of(
                        new Ingredient("1", "Flour", 200.0, Unit.G),
                        new Ingredient("2", "Milk", 300.0, Unit.ML),
                        new Ingredient("3", "Eggs", 2.0, Unit.PIECE)
                ),
                "Fluffy pancakes for breakfast"
        );
        Recipe r2 = new Recipe("2", "Omelette", 2.0,
                List.of(
                        new Ingredient("4", "Eggs", 3.0, Unit.PIECE),
                        new Ingredient("5", "Cheese", 50.0, Unit.G),
                        new Ingredient("6", "Salt", 1.0, Unit.G)
                ),
                "Quick and easy omelette"
        );
        List<Recipe> recipes = List.of(r1, r2);
        when(repository.findAll()).thenReturn(recipes);

        List<Recipe> actual = service.getAllRecipes();

        verify(repository).findAll();
        assertEquals(recipes, actual);
    }

    @Test
    void createRecipe() {
        RecipeDTO dto = new RecipeDTO(
                "French Toast",
                2.0,
                List.of(
                        new Ingredient("1", "Bread slices", 4.0, Unit.PIECE),
                        new Ingredient("2", "Milk", 100.0, Unit.ML),
                        new Ingredient("3", "Eggs", 2.0, Unit.PIECE)
                ),
                "Classic sweet French toast"
        );

        Recipe expected = new Recipe(
                "test-id-123",
                "French Toast",
                2.0,
                List.of(
                        new Ingredient("1", "Bread slices", 4.0, Unit.PIECE),
                        new Ingredient("2", "Milk", 100.0, Unit.ML),
                        new Ingredient("3", "Eggs", 2.0, Unit.PIECE)
                ),
                "Classic sweet French toast"
        );

        when(idService.generateId()).thenReturn("test-id-123");
        when(repository.save(expected)).thenReturn(expected);

        Recipe actual = service.createRecipe(dto);

        verify(idService).generateId();
        verify(repository).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateRecipe() {
        String id = "1";

        RecipeDTO dto = new RecipeDTO(
                "Vegetable Omelette",
                1.0,
                List.of(
                        new Ingredient("4", "Eggs", 2.0, Unit.PIECE),
                        new Ingredient("5", "Bell Pepper", 50.0, Unit.G),
                        new Ingredient("6", "Onion", 30.0, Unit.G)
                ),
                "Healthy vegetable omelette"
        );

        Recipe existing = new Recipe(
                id,
                "Omelette",
                1.0,
                List.of(
                        new Ingredient("4", "Eggs", 2.0, Unit.PIECE),
                        new Ingredient("5", "Cheese", 50.0, Unit.G)
                ),
                "Old description"
        );

        Recipe expected = new Recipe(
                id,
                "Vegetable Omelette",
                1.0,
                List.of(
                        new Ingredient("4", "Eggs", 2.0, Unit.PIECE),
                        new Ingredient("5", "Bell Pepper", 50.0, Unit.G),
                        new Ingredient("6", "Onion", 30.0, Unit.G)
                ),
                "Healthy vegetable omelette"
        );

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(expected)).thenReturn(expected);

        Recipe actual = service.updateRecipe(id, dto);

        verify(repository).findById(id);
        verify(repository).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getRecipeById_whenValidId_thenReturnRecipe() {
        String id = "1";
        Recipe r = new Recipe(
                id,
                "Pancakes",
                4.0,
                List.of(
                        new Ingredient("1", "Flour", 200.0, Unit.G),
                        new Ingredient("2", "Milk", 300.0, Unit.ML),
                        new Ingredient("3", "Eggs", 2.0, Unit.PIECE)
                ),
                "Fluffy pancakes"
        );
        when(repository.findById(id)).thenReturn(Optional.of(r));

        Recipe actual = service.getRecipeById(id);

        verify(repository).findById(id);
        assertEquals(r, actual);
    }

    @Test
    void getRecipeById_whenInvalidId_thenThrowException() {
        String id = "99";
        when(repository.findById(id)).thenReturn(Optional.empty());

        RecipeNotFoundException exception = assertThrows(
                RecipeNotFoundException.class,
                () -> service.getRecipeById(id)
        );

        assertEquals("Recipe not found with id " + id, exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    void deleteRecipeById() {
        String id = "1";
        doNothing().when(repository).deleteById(id);

        service.deleteRecipe(id);

        verify(repository).deleteById(id);
    }
}
