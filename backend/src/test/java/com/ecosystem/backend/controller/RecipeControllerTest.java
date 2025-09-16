package com.ecosystem.backend.controller;

import com.ecosystem.backend.models.Ingredient;
import com.ecosystem.backend.models.Recipe;
import com.ecosystem.backend.models.Unit;
import com.ecosystem.backend.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class RecipeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RecipeRepository recipeRepo;

    @BeforeEach
    void clearDb() {

        // GIVEN
        recipeRepo.deleteAll();
    }

    @Test
    void getAllRecipes_initiallyEmpty() throws Exception {

        // WHEN+THEN
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void createRecipe() throws Exception {

        // GIVEN
        String json = """
                {
                  "name": "Kartoffelsalat",
                  "servings": 3,
                  "ingredients": [
                    {"name": "Kartoffeln", "amount": 800.0, "unit": "G"},
                    {"name": "Essiggurken", "amount": 120.0, "unit": "G"},
                    {"name": "Zwiebel", "amount": 100.0, "unit": "G"}
                  ],
                  "description": "Klassischer Kartoffelsalat mit Essig-Öl-Dressing"
                }
                """;

        // WHEN+THEN
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Kartoffelsalat"))
                .andExpect(jsonPath("$.servings").value(3.0))
                .andExpect(jsonPath("$.ingredients.length()").value(3))
                .andExpect(jsonPath("$.ingredients[0].id").isNotEmpty())
                .andExpect(jsonPath("$.ingredients[0].name").value("Kartoffeln"))
                .andExpect(jsonPath("$.ingredients[1].id").isNotEmpty())
                .andExpect(jsonPath("$.ingredients[1].name").value("Essiggurken"))
                .andExpect(jsonPath("$.ingredients[2].id").isNotEmpty())
                .andExpect(jsonPath("$.ingredients[2].name").value("Zwiebel"))
                .andExpect(jsonPath("$.description").value("Klassischer Kartoffelsalat mit Essig-Öl-Dressing"));
    }

    @Test
    void updateRecipe() throws Exception {

        // GIVEN
        Recipe existing = new Recipe(
                "r1",
                "Tomatensuppe",
                2.0,
                List.of(
                        new Ingredient("i1", "Tomaten", 400.0, Unit.G),
                        new Ingredient("i2", "Zwiebel", 100.0, Unit.G)
                ),
                "Einfache Suppe"
        );
        recipeRepo.save(existing);

        String updateJson = """
                {
                  "name": "Geröstete Tomatensuppe",
                  "servings": 3,
                  "ingredients": [
                    {"id": "i1", "name": "Tomaten", "amount": 600.0, "unit": "G"},
                    {"id": "i2", "name": "Zwiebel", "amount": 100.0, "unit": "G"},
                    {"name": "Knoblauch", "amount": 50.0, "unit": "G"}
                  ],
                  "description": "Tomatensuppe mit Ofenaroma"
                }
                """;

        // WHEN+THEN
        mockMvc.perform(put("/api/recipes/r1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("r1"))
                .andExpect(jsonPath("$.name").value("Geröstete Tomatensuppe"))
                .andExpect(jsonPath("$.servings").value(3.0))
                .andExpect(jsonPath("$.ingredients.length()").value(3))
                .andExpect(jsonPath("$.ingredients[0].id").value("i1"))
                .andExpect(jsonPath("$.ingredients[0].name").value("Tomaten"))
                .andExpect(jsonPath("$.ingredients[1].id").value("i2"))
                .andExpect(jsonPath("$.ingredients[1].name").value("Zwiebel"))
                .andExpect(jsonPath("$.ingredients[2].id").isNotEmpty())
                .andExpect(jsonPath("$.ingredients[2].name").value("Knoblauch"))
                .andExpect(jsonPath("$.description").value("Tomatensuppe mit Ofenaroma"));
    }

    @Test
    void getRecipeById() throws Exception {

        // GIVEN
        Recipe existing = new Recipe(
                "r2",
                "Linsencurry",
                4.0,
                List.of(
                        new Ingredient("i1", "Rote Linsen", 250.0, Unit.G),
                        new Ingredient("i2", "Kokosmilch", 400.0, Unit.ML),
                        new Ingredient("i3", "Zwiebel", 100.0, Unit.G)
                ),
                "Cremiges Curry"
        );
        recipeRepo.save(existing);

        // WHEN+THEN
        mockMvc.perform(get("/api/recipes/r2"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("r2"))
                .andExpect(jsonPath("$.name").value("Linsencurry"))
                .andExpect(jsonPath("$.servings").value(4.0))
                .andExpect(jsonPath("$.ingredients.length()").value(3))
                .andExpect(jsonPath("$.ingredients[0].id").value("i1"))
                .andExpect(jsonPath("$.ingredients[0].name").value("Rote Linsen"))
                .andExpect(jsonPath("$.ingredients[1].id").value("i2"))
                .andExpect(jsonPath("$.ingredients[1].name").value("Kokosmilch"))
                .andExpect(jsonPath("$.ingredients[2].id").value("i3"))
                .andExpect(jsonPath("$.ingredients[2].name").value("Zwiebel"))
                .andExpect(jsonPath("$.description").value("Cremiges Curry"));
    }

    @Test
    void getRecipeById_notFound() throws Exception {

        // WHEN+THEN
        mockMvc.perform(get("/api/recipes/does-not-exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRecipe() throws Exception {

        // GIVEN
        Recipe existing = new Recipe(
                "r3",
                "Brotzeit",
                1.0,
                List.of(
                        new Ingredient("i1", "Brot", 200.0, Unit.G),
                        new Ingredient("i2", "Käse", 80.0, Unit.G)
                ),
                "Einfach und gut"
        );
        recipeRepo.save(existing);

        // WHEN+THEN
        mockMvc.perform(delete("/api/recipes/r3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRecipe_notFound() throws Exception {

        // WHEN+THEN
        mockMvc.perform(delete("/api/recipes/missing"))
                .andExpect(status().isNotFound());
    }
}