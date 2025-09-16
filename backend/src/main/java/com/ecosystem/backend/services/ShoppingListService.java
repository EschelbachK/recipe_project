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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShoppingListService {

    private final ShoppingListRepository repository;

    public ShoppingListService(ShoppingListRepository repository) {
        this.repository = repository;
    }

    public List<ShoppingListItem> getList() {
        return repository.findAllByUserId(getCurrentUserId());
    }

    public List<ShoppingListItem> addItems(ShoppingListAddRequest request) {
        String userId = getCurrentUserId();
        List<ShoppingListItem> oldItems = repository.findAllByUserIdAndRecipeId(userId, request.recipeId());
        repository.deleteAll(oldItems);

        for (ShoppingListAddRequest.IngredientPortion p : request.items()) {
            Optional<ShoppingListItem> existing = repository.findByUserIdAndNameAndUnit(userId, p.name(), p.unit());
            if (existing.isPresent()) {
                ShoppingListItem item = existing.get();
                BigDecimal newAmount = item.amount().add(p.amount());
                ShoppingListItem updated = new ShoppingListItem(
                        item.id(), userId, item.recipeId(),
                        item.ingredientId(), item.name(),
                        newAmount, item.unit(), item.done()
                );
                repository.save(updated);
            } else {
                ShoppingListItem created = new ShoppingListItem(
                        UUID.randomUUID().toString(), userId, request.recipeId(),
                        p.ingredientId(), p.name(), p.amount(), p.unit(), false
                );
                repository.save(created);
            }
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