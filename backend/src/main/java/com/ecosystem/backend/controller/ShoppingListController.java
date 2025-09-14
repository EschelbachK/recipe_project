package com.ecosystem.backend.controller;

import com.ecosystem.backend.dto.ShoppingListAddRequest;
import com.ecosystem.backend.dto.ShoppingListUpdateRequest;
import com.ecosystem.backend.models.ShoppingListItem;
import com.ecosystem.backend.services.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    private final ShoppingListService service;

    public ShoppingListController(ShoppingListService service) {
        this.service = service;
    }

    @GetMapping
    public List<ShoppingListItem> getList() {
        return service.getList();
    }

    @PostMapping("/add")
    public List<ShoppingListItem> addItems(@RequestBody ShoppingListAddRequest request) {
        return service.addItems(request);
    }

    @PutMapping("/{id}")
    public ShoppingListItem updateItem(
            @PathVariable String id,
            @RequestBody ShoppingListUpdateRequest request
    ) {
        return service.updateItem(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String id) {
        service.deleteItem(id);
    }

    @DeleteMapping("/recipe/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemsByRecipe(@PathVariable String recipeId) {
        service.deleteItemsByRecipe(recipeId);
    }
}