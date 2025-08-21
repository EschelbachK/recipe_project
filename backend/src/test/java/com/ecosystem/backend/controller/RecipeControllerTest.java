package com.ecosystem.backend.controller;

import com.ecosystem.backend.models.Ingredient;
import com.ecosystem.backend.models.Recipe;
import com.ecosystem.backend.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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

    @Test
    void getAllRecipes() throws Exception {
        mockMvc.perform(get("/api/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void createRecipe() throws Exception {
        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Test Recipe",
                                  "description": "Test Description",
                                  "ingredients": [
                                    {"name": "Zutat1", "amount": 100.0, "unit": "g"}
                                  ],
                                  "instructions": ["Schritt1"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(content().json("""
                        {
                          "title": "Test Recipe",
                          "description": "Test Description",
                          "ingredients": [
                            {"name": "Zutat1", "amount": 100.0, "unit": "g"}
                          ],
                          "instructions": ["Schritt1"]
                        }
                        """));
    }

    @Test
    @DirtiesContext
    void updateRecipe() throws Exception {
        Recipe existing = new Recipe("1", "Old Recipe", "Old Desc",
                List.of(new Ingredient("Zutat1", 100.0, "g")), List.of("Schritt1"));
        recipeRepo.save(existing);

        mockMvc.perform(put("/api/recipe/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Updated Recipe",
                                  "description": "Updated Desc",
                                  "ingredients": [
                                    {"name": "Zutat2", "amount": 200.0, "unit": "ml"}
                                  ],
                                  "instructions": ["Schritt2"]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "1",
                          "title": "Updated Recipe",
                          "description": "Updated Desc",
                          "ingredients": [
                            {"name": "Zutat2", "amount": 200.0, "unit": "ml"}
                          ],
                          "instructions": ["Schritt2"]
                        }
                        """));
    }

    @Test
    @DirtiesContext
    void getRecipeById() throws Exception {
        Recipe existing = new Recipe("1", "Test Recipe", "Desc",
                List.of(new Ingredient("Zutat1", 100.0, "g")), List.of("Schritt1"));
        recipeRepo.save(existing);

        mockMvc.perform(get("/api/recipe/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "id": "1",
                          "title": "Test Recipe",
                          "description": "Desc",
                          "ingredients": [
                            {"name": "Zutat1", "amount": 100.0, "unit": "g"}
                          ],
                          "instructions": ["Schritt1"]
                        }
                        """));
    }

    @Test
    void getRecipeById_notFound() throws Exception {
        mockMvc.perform(get("/api/recipe/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void deleteRecipe() throws Exception {
        Recipe existing = new Recipe("1", "Test Recipe", "Desc",
                List.of(new Ingredient("Zutat1", 100.0, "g")), List.of("Schritt1"));
        recipeRepo.save(existing);

        mockMvc.perform(delete("/api/recipe/1"))
                .andExpect(status().isOk());
    }
}
