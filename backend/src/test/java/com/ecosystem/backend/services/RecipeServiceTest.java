package com.ecosystem.backend.services;

import com.ecosystem.backend.dto.RecipeDTO;
import com.ecosystem.backend.exception.RecipeNotFoundException;
import com.ecosystem.backend.models.Ingredient;
import com.ecosystem.backend.models.Recipe;
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
        Recipe r1 = new Recipe("1", "Titel1", "Beschreibung1",
                List.of(new Ingredient("Zutat1", 100.0, "g")),
                List.of("Schritt1"));
        Recipe r2 = new Recipe("2", "Titel2", "Beschreibung2",
                List.of(new Ingredient("Zutat2", 200.0, "ml")),
                List.of("Schritt2"));
        List<Recipe> rezepte = List.of(r1, r2);
        when(repository.findAll()).thenReturn(rezepte);

        List<Recipe> actual = service.getAllRecipes();

        verify(repository).findAll();
        assertEquals(rezepte, actual);
    }

    @Test
    void createRecipe() {
        RecipeDTO dto = new RecipeDTO(
                "Neues Rezept",
                "Beschreibung",
                List.of(new Ingredient("Zutat1", 100.0, "g")),
                List.of("Schritt1")
        );
        Recipe expected = new Recipe(
                "test-id-123",
                "Neues Rezept",
                "Beschreibung",
                List.of(new Ingredient("Zutat1", 100.0, "g")),
                List.of("Schritt1")
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
                "Aktualisiert",
                "Neue Beschreibung",
                List.of(new Ingredient("Neue Zutat", 250.0, "g")),
                List.of("Neuer Schritt")
        );
        Recipe existing = new Recipe(
                id,
                "Alt",
                "Alte Beschreibung",
                List.of(new Ingredient("Zutat1", 100.0, "g")),
                List.of("Schritt1")
        );
        Recipe expected = new Recipe(
                id,
                "Aktualisiert",
                "Neue Beschreibung",
                List.of(new Ingredient("Neue Zutat", 250.0, "g")),
                List.of("Neuer Schritt")
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
                "Titel",
                "Beschreibung",
                List.of(new Ingredient("Zutat1", 100.0, "g")),
                List.of("Schritt1")
        );
        when(repository.findById(id)).thenReturn(Optional.of(r));

        Recipe actual = service.getRecipeById(id);

        verify(repository).findById(id);
        assertEquals(r, actual);
    }

    @Test
    void getRecipeById_whenInvalidId_thenThrowException() {
        String id = "2";
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
