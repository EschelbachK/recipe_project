package com.ecosystem.backend.controller;

import com.ecosystem.backend.models.Recipe;
import com.ecosystem.backend.repository.RecipeRepository;
import com.ecosystem.backend.services.FavoritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoritesController {

    private final FavoritesService favoritesService;
    private final RecipeRepository recipeRepository;

    @GetMapping("/recipes")
    public List<Recipe> getFavoriteRecipes(@AuthenticationPrincipal OAuth2User user) {
        List<String> ids = favoritesService.getFavoriteIds(user.getName());
        return recipeRepository.findAllById(ids);
    }

    @PostMapping("/{recipeId}/toggle")
    public void toggleFavorite(@PathVariable String recipeId, @AuthenticationPrincipal OAuth2User user) {
        favoritesService.toggleFavorite(user.getName(), recipeId);
    }
}
