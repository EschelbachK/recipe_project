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
        Recipe r1 = new Recipe("r1", "Pasta", 2.0,
                List.of(
                        new Ingredient("i1", "Nudeln", 200.0, Unit.G),
                        new Ingredient("i2", "Öl", 20.0, Unit.ML)
                ),
                "Beschreibung"
        );
        Recipe r2 = new Recipe("r2", "Soup", 3.0,
                List.of(
                        new Ingredient("i3", "Wasser", 500.0, Unit.ML),
                        new Ingredient("i4", "Salz", 5.0, Unit.G)
                ),
                "Beschreibung"
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
                "Rice Bowl",
                1.0,
                List.of(
                        new Ingredient(null, "Reis", 100.0, Unit.G),
                        new Ingredient(null, "Sojasauce", 10.0, Unit.ML)
                ),
                "Beschreibung"
        );

        Recipe expected = new Recipe(
                "testId123",
                "Rice Bowl",
                1.0,
                List.of(
                        new Ingredient("geni1", "Reis", 100.0, Unit.G),
                        new Ingredient("geni2", "Sojasauce", 10.0, Unit.ML)
                ),
                "Beschreibung"
        );

        when(idService.generateId()).thenReturn("testId123", "geni1", "geni2");
        when(repository.save(any())).thenReturn(expected);

        // WHEN
        Recipe actual = service.createRecipe(dto);

        // THEN
        verify(idService, times(3)).generateId();
        verify(repository).save(any());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.ingredients().size(), actual.ingredients().size());
        assertNotNull(actual.ingredients().get(0).id());
        assertNotNull(actual.ingredients().get(1).id());
    }

    @Test
    void updateRecipe_whenExists_updatesAndReturns() {

        // GIVEN
        String id = "r1";
        RecipeDTO dto = new RecipeDTO(
                "Veggie Bowl",
                2.0,
                List.of(
                        new Ingredient("i1", "Reis", 150.0, Unit.G),
                        new Ingredient(null, "Avocado", 120.0, Unit.G)
                ),
                "Beschreibung"
        );

        Recipe toSave = new Recipe(
                id,
                "Veggie Bowl",
                2.0,
                List.of(
                        new Ingredient("i1", "Reis", 150.0, Unit.G),
                        new Ingredient("geni2", "Avocado", 120.0, Unit.G)
                ),
                "Beschreibung"
        );

        when(repository.existsById(id)).thenReturn(true);
        when(idService.generateId()).thenReturn("geni2");
        when(repository.save(any())).thenReturn(toSave);

        // WHEN
        Recipe actual = service.updateRecipe(id, dto);

        // THEN
        verify(repository).existsById(id);
        verify(repository).save(any());
        assertEquals("Veggie Bowl", actual.name());
        assertEquals(2, actual.ingredients().size());
        assertEquals("i1", actual.ingredients().get(0).id());
        assertEquals("geni2", actual.ingredients().get(1).id());
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
                "Toast",
                1.0,
                List.of(
                        new Ingredient("i1", "Brot", 200.0, Unit.G),
                        new Ingredient("i2", "Butter", 15.0, Unit.G)
                ),
                "Beschreibung"
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

    @Test
    void addMissingIngredientIds_keepsExistingIds() {

        // GIVEN
        RecipeDTO dto = new RecipeDTO(
                "Toast",
                1.0,
                List.of(
                        new Ingredient("i1", "Brot", 200.0, Unit.G),
                        new Ingredient("i2", "Käse", 50.0, Unit.G)
                ),
                "Beschreibung"
        );

        Recipe expected = new Recipe(
                "recipe123",
                "Toast",
                1.0,
                List.of(
                        new Ingredient("i1", "Brot", 200.0, Unit.G),
                        new Ingredient("i2", "Käse", 50.0, Unit.G)
                ),
                "Beschreibung"
        );

        when(idService.generateId()).thenReturn("recipe123");
        when(repository.save(any())).thenReturn(expected);

        // WHEN
        Recipe actual = service.createRecipe(dto);

        // THEN
        verify(idService, times(1)).generateId();
        assertEquals("i1", actual.ingredients().get(0).id());
        assertEquals("i2", actual.ingredients().get(1).id());
    }
}