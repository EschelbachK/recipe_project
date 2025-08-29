package com.ecosystem.backend.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    @Test
    void appUser_builderCreatesUserWithAllFields() {
        AppUser user = AppUser.builder()
                .id("42")
                .username("testuser")
                .avatarUrl("http://img")
                .build();

        assertEquals("42", user.id());
        assertEquals("testuser", user.username());
        assertEquals("http://img", user.avatarUrl());
    }

    @Test
    void appUser_builderHandlesNullFields() {
        AppUser user = AppUser.builder()
                .id(null)
                .username(null)
                .avatarUrl(null)
                .build();

        assertNull(user.id());
        assertNull(user.username());
        assertNull(user.avatarUrl());
    }
}
