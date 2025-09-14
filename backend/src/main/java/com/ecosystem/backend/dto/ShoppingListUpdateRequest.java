package com.ecosystem.backend.dto;

import java.math.BigDecimal;

public record ShoppingListUpdateRequest(
        BigDecimal amount,
        Boolean done
) {}