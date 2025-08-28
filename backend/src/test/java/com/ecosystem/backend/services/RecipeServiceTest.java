package com.ecosystem.backend.services;

import com.ecosystem.backend.dto.RecipeDTO;
import com.ecosystem.backend.exception.RecipeNotFoundException;
import com.ecosystem.backend.models.Ingredient;
import com.ecosystem.backend.models.Recipe;
import com.ecosystem.backend.models.Unit;
import com.ecosystem.backend.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    RecipeRepository repository;
    IdService idService;
    RecipeService service;

    @BeforeEach
    void setup() {

        // GIVEN
        repository = Mockito.mock(RecipeRepository.class);
        idService = Mockito.mock(IdService.class);
        service = new RecipeService(repository, idService);
    }

    @Test
    void getAllRecipes_returnsList() {

        // GIVEN
        Recipe r1 = new Recipe("r1", "Kartoffelsalat", 3.0,
                List.of(
                        new Ingredient("i1", "Kartoffeln", 800.0, Unit.G),
                        new Ingredient("i2", "Essiggurken", 120.0, Unit.G),
                        new Ingredient("i3", "Zwiebel", 1.0, Unit.PIECE)
                ),
                "Mit Essig-Öl-Dressing"
        );
        Recipe r2 = new Recipe("r2", "Tomatensuppe", 2.0,
                List.of(
                        new Ingredient("i4", "Tomaten", 600.0, Unit.G),
                        new Ingredient("i5", "Zwiebel", 1.0, Unit.PIECE)
                ),
                "Geröstete Tomaten"
        );
        List<Recipe> recipes = List.of(r1, r2);
        when(repository.findAll()).thenReturn(recipes);

        // WHEN
        List<Recipe> actual = service.getAllRecipes();

        // THEN
        verify(repository).findAll();
        assertEquals(recipes, actual);
    }

    @Test
    void createRecipe_generatesIdAndSaves() {

        // GIVEN
        RecipeDTO dto = new RecipeDTO(
                "Linsencurry",
                4.0,
                List.of(
                        new Ingredient("i1", "Rote Linsen", 250.0, Unit.G),
                        new Ingredient("i2", "Kokosmilch", 400.0, Unit.ML),
                        new Ingredient("i3", "Zwiebel", 1.0, Unit.PIECE)
                ),
                "Cremig und mild"
        );

        Recipe expected = new Recipe(
                "test-id-123",
                "Linsencurry",
                4.0,
                List.of(
                        new Ingredient("i1", "Rote Linsen", 250.0, Unit.G),
                        new Ingredient("i2", "Kokosmilch", 400.0, Unit.ML),
                        new Ingredient("i3", "Zwiebel", 1.0, Unit.PIECE)
                ),
                "Cremig und mild"
        );

        when(idService.generateId()).thenReturn("test-id-123");
        when(repository.save(expected)).thenReturn(expected);

        // WHEN
        Recipe actual = service.createRecipe(dto);

        // THEN
        verify(idService).generateId();
        verify(repository).save(expected);
        assertEquals(expected, actual);
    }

    @Test
    void updateRecipe_whenExists_updatesAndReturns() {

        // GIVEN
        String id = "r1";
        RecipeDTO dto = new RecipeDTO(
                "Geröstete Tomatensuppe",
                3.0,
                List.of(
                        new Ingredient("i1", "Tomaten", 600.0, Unit.G),
                        new Ingredient("i2", "Zwiebel", 1.0, Unit.PIECE),
                        new Ingredient("i3", "Knoblauch", 1.0, Unit.PIECE)
                ),
                "Mit Ofenaroma"
        );

        Recipe toSave = new Recipe(
                id,
                "Geröstete Tomatensuppe",
                3.0,
                List.of(
                        new Ingredient("i1", "Tomaten", 600.0, Unit.G),
                        new Ingredient("i2", "Zwiebel", 1.0, Unit.PIECE),
                        new Ingredient("i3", "Knoblauch", 1.0, Unit.PIECE)
                ),
                "Mit Ofenaroma"
        );

        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(toSave)).thenReturn(toSave);

        // WHEN
        Recipe actual = service.updateRecipe(id, dto);

        // THEN
        verify(repository).existsById(id);
        verify(repository).save(toSave);
        assertEquals(toSave, actual);
    }

    @Test
    void updateRecipe_whenMissing_throwsNotFound() {

        // GIVEN
        String id = "nicht gefunden";
        when(repository.existsById(id)).thenReturn(false);

        RecipeDTO dto = new RecipeDTO("x", 1.0, List.of(), "x");

        // WHEN+THEN
        RecipeNotFoundException ex = assertThrows(
                RecipeNotFoundException.class,
                () -> service.updateRecipe(id, dto)
        );
        assertTrue(ex.getMessage().contains(id));
        verify(repository).existsById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void getRecipeById_whenValid_returnsRecipe() {

        // GIVEN
        String id = "r2";
        Recipe r = new Recipe(
                id,
                "Brotzeit",
                1.0,
                List.of(
                        new Ingredient("i1", "Brot", 2.0, Unit.PIECE),
                        new Ingredient("i2", "Käse", 80.0, Unit.G)
                ),
                "Einfach und gut"
        );
        when(repository.findById(id)).thenReturn(java.util.Optional.of(r));

        // WHEN
        Recipe actual = service.getRecipeById(id);

        // THEN
        verify(repository).findById(id);
        assertEquals(r, actual);
    }

    @Test
    void getRecipeById_whenInvalid_throwsNotFound() {

        // GIVEN
        String id = "nicht gefunden";
        when(repository.findById(id)).thenReturn(java.util.Optional.empty());

        // WHEN+THEN
        RecipeNotFoundException ex = assertThrows(
                RecipeNotFoundException.class,
                () -> service.getRecipeById(id)
        );
        assertTrue(ex.getMessage().contains(id));
        verify(repository).findById(id);
    }

    @Test
    void deleteRecipe_whenExists_deletes() {

        // GIVEN
        String id = "r3";
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        // WHEN
        service.deleteRecipe(id);

        // THEN
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deleteRecipe_whenMissing_throwsNotFound() {

        // GIVEN
        String id = "nicht gefunden";
        when(repository.existsById(id)).thenReturn(false);

        // WHEN+THEN
        assertThrows(RecipeNotFoundException.class, () -> service.deleteRecipe(id));
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyString());
    }
}
