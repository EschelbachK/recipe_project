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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RecipeRepository recipeRepo;

    @BeforeEach
    void cleanDb() {
        recipeRepo.deleteAll();
    }

    @Test
    void getAllRecipes_initiallyEmpty() throws Exception {
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createRecipe() throws Exception {
        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Pancakes",
                                  "servings": 4,
                                  "ingredients": [
                                    {"id": "1", "name": "Flour", "amount": 200.0, "unit": "G"},
                                    {"id": "2", "name": "Milk", "amount": 300.0, "unit": "ML"},
                                    {"id": "3", "name": "Eggs", "amount": 2.0, "unit": "PIECE"}
                                  ],
                                  "description": "Simple fluffy pancakes"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Pancakes"))
                .andExpect(jsonPath("$.servings").value(4.0))
                .andExpect(jsonPath("$.ingredients.length()").value(3))
                .andExpect(jsonPath("$.ingredients[0].name").value("Flour"))
                .andExpect(jsonPath("$.ingredients[1].name").value("Milk"))
                .andExpect(jsonPath("$.ingredients[2].name").value("Eggs"))
                .andExpect(jsonPath("$.description").value("Simple fluffy pancakes"));
    }

    @Test
    void updateRecipe() throws Exception {
        Recipe existing = new Recipe(
                "1",
                "Basic Pancakes",
                2.0,
                List.of(
                        new Ingredient("1", "Flour", 150.0, Unit.G),
                        new Ingredient("2", "Milk", 200.0, Unit.ML)
                ),
                "Old pancake recipe"
        );
        recipeRepo.save(existing);

        mockMvc.perform(put("/api/recipes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Banana Pancakes",
                                  "servings": 4,
                                  "ingredients": [
                                    {"id": "1", "name": "Flour", "amount": 200.0, "unit": "G"},
                                    {"id": "2", "name": "Milk", "amount": 300.0, "unit": "ML"},
                                    {"id": "3", "name": "Banana", "amount": 2.0, "unit": "PIECE"}
                                  ],
                                  "description": "Pancakes with mashed bananas"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Banana Pancakes"))
                .andExpect(jsonPath("$.servings").value(4.0))
                .andExpect(jsonPath("$.ingredients.length()").value(3))
                .andExpect(jsonPath("$.ingredients[0].name").value("Flour"))
                .andExpect(jsonPath("$.ingredients[1].name").value("Milk"))
                .andExpect(jsonPath("$.ingredients[2].name").value("Banana"))
                .andExpect(jsonPath("$.description").value("Pancakes with mashed bananas"));
    }

    @Test
    void getRecipeById() throws Exception {
        Recipe existing = new Recipe(
                "1",
                "Omelette",
                1.0,
                List.of(
                        new Ingredient("1", "Eggs", 3.0, Unit.PIECE),
                        new Ingredient("2", "Cheese", 50.0, Unit.G),
                        new Ingredient("3", "Salt", 1.0, Unit.G)
                ),
                "Quick breakfast omelette"
        );
        recipeRepo.save(existing);

        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Omelette"))
                .andExpect(jsonPath("$.servings").value(1.0))
                .andExpect(jsonPath("$.ingredients.length()").value(3))
                .andExpect(jsonPath("$.ingredients[0].name").value("Eggs"))
                .andExpect(jsonPath("$.ingredients[1].name").value("Cheese"))
                .andExpect(jsonPath("$.ingredients[2].name").value("Salt"))
                .andExpect(jsonPath("$.description").value("Quick breakfast omelette"));
    }

    @Test
    void getRecipeById_notFound() throws Exception {
        mockMvc.perform(get("/api/recipes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteRecipe() throws Exception {
        Recipe existing = new Recipe(
                "1",
                "Test Recipe",
                1.0,
                List.of(new Ingredient("1", "Ingredient1", 100.0, Unit.G)),
                "Desc"
        );
        recipeRepo.save(existing);

        mockMvc.perform(delete("/api/recipes/1"))
                .andExpect(status().isNoContent());
    }
}
