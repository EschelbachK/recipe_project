package com.ecosystem.backend.services;

import com.ecosystem.backend.dto.ShoppingListAddRequest;
import com.ecosystem.backend.dto.ShoppingListUpdateRequest;
import com.ecosystem.backend.exception.ShoppingListItemNotFoundException;
import com.ecosystem.backend.models.ShoppingListItem;
import com.ecosystem.backend.repository.ShoppingListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingListServiceTest {

    private ShoppingListRepository repository;
    private ShoppingListService service;

    @BeforeEach
    void setup() {

        // GIVEN
        repository = mock(ShoppingListRepository.class);
        service = new ShoppingListService(repository);
    }

    @Test
    void addNewItemStoresIt() {

        // GIVEN
        when(repository.findAllByUserIdAndRecipeId(anyString(), anyString())).thenReturn(List.of());
        ShoppingListAddRequest req = new ShoppingListAddRequest(
                "r1",
                List.of(new ShoppingListAddRequest.IngredientPortion("ing1", "Tomate", new BigDecimal("200"), "G"))
        );

        // WHEN
        service.addItems(req);

        // THEN
        verify(repository).save(any(ShoppingListItem.class));
    }

    @Test
    void getListSumsItemsByNameAndUnit() {

        // GIVEN
        ShoppingListItem i1 = new ShoppingListItem("1", "user", "r1", "ing1", "Tomate", new BigDecimal("100"), "G", false);
        ShoppingListItem i2 = new ShoppingListItem("2", "user", "r2", "ing2", "Tomate", new BigDecimal("200"), "G", false);
        when(repository.findAllByUserId(anyString())).thenReturn(List.of(i1, i2));

        // WHEN
        List<ShoppingListItem> result = service.getList();

        // THEN
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("300"), result.getFirst().amount());
    }

    @Test
    void updateItemChangesFields() {

        // GIVEN
        ShoppingListItem existing = new ShoppingListItem(
                "1", "default-user", "r1", "ing1", "Tomate", new BigDecimal("200"), "G", false
        );
        when(repository.findByIdAndUserId(eq("1"), anyString())).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
        ShoppingListUpdateRequest req = new ShoppingListUpdateRequest(new BigDecimal("500"), true);

        // WHEN
        ShoppingListItem updated = service.updateItem("1", req);

        // THEN
        assertEquals(new BigDecimal("500"), updated.amount());
        assertTrue(updated.done());
    }

    @Test
    void updateItemThrowsWhenNotFound() {

        // GIVEN
        when(repository.findByIdAndUserId(eq("x1"), anyString())).thenReturn(Optional.empty());
        ShoppingListUpdateRequest req = new ShoppingListUpdateRequest(new BigDecimal("100"), true);

        // WHEN+THEN
        assertThrows(ShoppingListItemNotFoundException.class, () -> service.updateItem("x1", req));
    }

    @Test
    void deleteItemThrowsWhenNotFound() {

        // GIVEN
        when(repository.findByIdAndUserId(eq("x2"), anyString())).thenReturn(Optional.empty());

        // WHEN+THEN
        assertThrows(ShoppingListItemNotFoundException.class, () -> service.deleteItem("x2"));
    }

    @Test
    void deleteItemsByRecipe_deletesAll() {

        // GIVEN
        when(repository.findAllByUserIdAndRecipeId(anyString(), eq("r1")))
                .thenReturn(List.of(new ShoppingListItem("1", "user", "r1", "ing1", "Tomate", BigDecimal.ONE, "G", false)));

        // WHEN
        service.deleteItemsByRecipe("r1");

        // THEN
        verify(repository).deleteAll(anyList());
    }
}