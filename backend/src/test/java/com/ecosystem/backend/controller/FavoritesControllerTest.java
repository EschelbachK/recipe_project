package com.ecosystem.backend.controller;

import com.ecosystem.backend.models.Favorites;
import com.ecosystem.backend.models.Recipe;
import com.ecosystem.backend.repository.FavoritesRepository;
import com.ecosystem.backend.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FavoritesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FavoritesRepository favoritesRepo;

    @Autowired
    RecipeRepository recipeRepo;

    private static final String USER_ID = "user123";
    private static final String R1 = "spaghetti";
    private static final String R2 = "tortellini";

    @BeforeEach
    void clearDb() {
        favoritesRepo.deleteAll();
        recipeRepo.deleteAll();
    }

    @Test
    void shouldAddRecipeOnToggle() throws Exception {

        // GIVEN
        favoritesRepo.save(new Favorites(null, USER_ID, List.of()));

        // WHEN+THEN
        mockMvc.perform(post("/api/favorites/" + R1 + "/toggle")
                        .with(oidcLogin().idToken(t -> t.subject(USER_ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRemoveRecipeOnToggle() throws Exception {

        // GIVEN
        favoritesRepo.save(new Favorites(null, USER_ID, List.of(R1, R2)));

        // WHEN+THEN
        mockMvc.perform(post("/api/favorites/" + R1 + "/toggle")
                        .with(oidcLogin().idToken(t -> t.subject(USER_ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFetchFavoriteRecipes() throws Exception {

        // GIVEN
        Recipe rec = new Recipe(R1, "Pizza", 3.0, List.of(), "mit Salami");
        recipeRepo.save(rec);
        favoritesRepo.save(new Favorites(null, USER_ID, List.of(R1)));

        // WHEN+THEN
        mockMvc.perform(get("/api/favorites/recipes")
                        .with(oidcLogin().idToken(t -> t.subject(USER_ID)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(R1))
                .andExpect(jsonPath("$[0].name").value("Pizza"));
    }
}