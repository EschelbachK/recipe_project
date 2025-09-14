package com.ecosystem.backend.controller;

import com.ecosystem.backend.models.ShoppingListItem;
import com.ecosystem.backend.repository.ShoppingListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user")
class ShoppingListControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ShoppingListRepository shoppingListRepo;

    @BeforeEach
    void clearDb() {
        shoppingListRepo.deleteAll();
    }

    @Test
    void getListInitiallyEmpty() throws Exception {

        // WHEN+THEN
        mockMvc.perform(get("/api/shopping-list"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void addItems() throws Exception {

        // GIVEN
        String body = """
                {
                  "recipeId": "r1",
                  "items": [
                    {"ingredientId":"ing1","name":"Tomate","amount":300,"unit":"G"}
                  ]
                }
                """;

        // WHEN+THEN
        mockMvc.perform(post("/api/shopping-list/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Tomate"))
                .andExpect(jsonPath("$[0].amount").value(300.0));
    }

    @Test
    void updateItem() throws Exception {

        // GIVEN
        ShoppingListItem existing = new ShoppingListItem(
                "id1", "user", "r1", "ing1", "Tomate", new BigDecimal("100"), "G", false
        );
        shoppingListRepo.save(existing);

        String updateJson = """
                {"amount":500,"done":true}
                """;

        // WHEN+THEN
        mockMvc.perform(put("/api/shopping-list/" + existing.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(500.0))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void deleteItem() throws Exception {

        // GIVEN
        ShoppingListItem existing = new ShoppingListItem(
                "id2", "user", "r1", "ing2", "Gurke", new BigDecimal("200"), "G", false
        );
        shoppingListRepo.save(existing);

        // WHEN+THEN
        mockMvc.perform(delete("/api/shopping-list/" + existing.id()))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteItemsByRecipe() throws Exception {

        // GIVEN
        ShoppingListItem existing1 = new ShoppingListItem(
                "id3", "user", "r99", "ing5", "Apfel", new BigDecimal("100"), "G", false
        );
        ShoppingListItem existing2 = new ShoppingListItem(
                "id4", "user", "r99", "ing6", "Banane", new BigDecimal("200"), "G", false
        );
        shoppingListRepo.saveAll(List.of(existing1, existing2));

        // WHEN+THEN
        mockMvc.perform(delete("/api/shopping-list/recipe/r99"))
                .andExpect(status().isNoContent());
    }
}