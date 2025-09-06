package com.ecosystem.backend.repository;

import com.ecosystem.backend.models.Favorites;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FavoritesRepository extends MongoRepository<Favorites, String> {
    Optional<Favorites> findByUserId(String userId);
}
