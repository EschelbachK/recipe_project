package com.ecosystem.backend.services;

import com.ecosystem.backend.models.Favorites;
import com.ecosystem.backend.repository.FavoritesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;

    private Favorites getOrCreate(String userId) {
        return favoritesRepository.findByUserId(userId)
                .orElseGet(() -> favoritesRepository.save(new Favorites(null, userId, new ArrayList<>())));
    }

    public List<String> getFavoriteIds(String userId) {
        return new ArrayList<>(getOrCreate(userId).recipeIds());
    }

    public void toggleFavorite(String userId, String recipeId) {
        Favorites favorites = getOrCreate(userId);
        List<String> ids = new ArrayList<>(favorites.recipeIds());
        if (ids.contains(recipeId)) {
            ids.remove(recipeId);
        } else {
            ids.add(recipeId);
        }
        favoritesRepository.save(new Favorites(favorites.id(), favorites.userId(), ids));
    }
}
