package com.ecosystem.backend.services;

import com.ecosystem.backend.dto.ShoppingListAddRequest;
import com.ecosystem.backend.dto.ShoppingListUpdateRequest;
import com.ecosystem.backend.exception.ShoppingListItemNotFoundException;
import com.ecosystem.backend.models.ShoppingListItem;
import com.ecosystem.backend.repository.ShoppingListRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ShoppingListService {

    private final ShoppingListRepository repository;

    public ShoppingListService(ShoppingListRepository repository) {
        this.repository = repository;
    }

    public List<ShoppingListItem> getList() {
        String userId = getCurrentUserId();
        List<ShoppingListItem> allItems = repository.findAllByUserId(userId);

        Map<String, ShoppingListItem> grouped = new HashMap<>();
        for (ShoppingListItem item : allItems) {
            String key = item.name() + "|" + item.unit();
            if (grouped.containsKey(key)) {
                ShoppingListItem existing = grouped.get(key);
                BigDecimal newAmount = existing.amount().add(item.amount());
                grouped.put(key, new ShoppingListItem(
                        existing.id(), userId, existing.recipeId(),
                        existing.ingredientId(), existing.name(),
                        newAmount, existing.unit(), existing.done()
                ));
            } else {
                grouped.put(key, new ShoppingListItem(
                        item.id(), userId, item.recipeId(),
                        item.ingredientId(), item.name(),
                        item.amount(), item.unit(), item.done()
                ));
            }
        }

        return new ArrayList<>(grouped.values());
    }

    public List<ShoppingListItem> addItems(ShoppingListAddRequest request) {
        String userId = getCurrentUserId();
        List<ShoppingListItem> oldItems = repository.findAllByUserIdAndRecipeId(userId, request.recipeId());
        repository.deleteAll(oldItems);

        for (ShoppingListAddRequest.IngredientPortion p : request.items()) {
            ShoppingListItem created = new ShoppingListItem(
                    UUID.randomUUID().toString(), userId, request.recipeId(),
                    p.ingredientId(), p.name(), p.amount(), p.unit(), false
            );
            repository.save(created);
        }

        return getList();
    }

    public ShoppingListItem updateItem(String id, ShoppingListUpdateRequest request) {
        String userId = getCurrentUserId();
        ShoppingListItem item = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ShoppingListItemNotFoundException(id));

        BigDecimal amount = item.amount();
        if (request.amount() != null) {
            amount = request.amount();
        }

        boolean done = item.done();
        if (request.done() != null) {
            done = request.done();
        }

        ShoppingListItem updated = new ShoppingListItem(
                item.id(), userId, item.recipeId(),
                item.ingredientId(), item.name(),
                amount, item.unit(), done
        );

        return repository.save(updated);
    }

    public void deleteItem(String id) {
        String userId = getCurrentUserId();
        ShoppingListItem item = repository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ShoppingListItemNotFoundException(id));
        repository.delete(item);
    }

    public void deleteItemsByRecipe(String recipeId) {
        String userId = getCurrentUserId();
        List<ShoppingListItem> items = repository.findAllByUserIdAndRecipeId(userId, recipeId);
        repository.deleteAll(items);
    }

    private String getCurrentUserId() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || a.getName() == null || a.getName().isBlank()) return "default-user";
        return a.getName();
    }
}