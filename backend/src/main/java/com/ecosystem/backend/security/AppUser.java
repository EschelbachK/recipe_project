package com.ecosystem.backend.security;

import lombok.Builder;

@Builder
public record AppUser(
        String id,
        String username,
        String avatarUrl
) {
}