package com.ecosystem.backend.services;

import com.ecosystem.backend.models.Favorites;
import com.ecosystem.backend.repository.FavoritesRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoritesServiceTest {

    FavoritesRepository repository = Mockito.mock(FavoritesRepository.class);
    FavoritesService service = new FavoritesService(repository);

    private static final String USER_ID = "user123";
    private static final String R1 = "spaghetti";
    private static final String R2 = "pizza";

    @Test
    void shouldReturnEmptyListForNewUser() {

        // GIVEN
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(repository.save(any(Favorites.class)))
                .thenAnswer(inv -> new Favorites("fav1", USER_ID, new ArrayList<>()));

        // WHEN
        List<String> ids = service.getFavoriteIds(USER_ID);

        // THEN
        assertTrue(ids.isEmpty());
        verify(repository).findByUserId(USER_ID);
        verify(repository).save(any(Favorites.class));
    }

    @Test
    void shouldReturnExistingIds() {

        // GIVEN
        when(repository.findByUserId(USER_ID))
                .thenReturn(Optional.of(new Favorites("fav1", USER_ID, List.of(R1, R2))));

        // WHEN
        List<String> ids = service.getFavoriteIds(USER_ID);

        // THEN
        assertEquals(List.of(R1, R2), ids);
    }

    @Test
    void shouldAddIdOnToggle() {

        // GIVEN
        when(repository.findByUserId(USER_ID))
                .thenReturn(Optional.of(new Favorites("fav1", USER_ID, new ArrayList<>())));
        when(repository.save(any(Favorites.class)))
                .thenReturn(new Favorites("fav1", USER_ID, List.of(R1)));

        // WHEN
        service.toggleFavorite(USER_ID, R1);

        // THEN
        verify(repository).save(new Favorites("fav1", USER_ID, List.of(R1)));
    }

    @Test
    void shouldRemoveIdOnToggle() {

        // GIVEN
        when(repository.findByUserId(USER_ID))
                .thenReturn(Optional.of(new Favorites("fav1", USER_ID, List.of(R1, R2))));
        when(repository.save(any(Favorites.class)))
                .thenReturn(new Favorites("fav1", USER_ID, List.of(R2)));

        // WHEN
        service.toggleFavorite(USER_ID, R1);

        // THEN
        verify(repository).save(new Favorites("fav1", USER_ID, List.of(R2)));
    }
}
