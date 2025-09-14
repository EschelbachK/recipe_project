package com.ecosystem.backend.exception;

public class ShoppingListItemNotFoundException extends RuntimeException {
    public ShoppingListItemNotFoundException(String id) {
        super("Shopping list item not found: " + id);
    }
}