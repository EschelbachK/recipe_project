package com.ecosystem.backend.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMe_shouldReturnAppUser() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                        .with(oidcLogin()
                                .idToken(token -> token.claim("sub", "123"))
                                .userInfoToken(token -> token
                                        .claim("login", "testUser")
                                        .claim("avatar_url", "avatar.png"))))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                      "id": "123",
                      "username": "testUser",
                      "avatarUrl": "avatar.png"
                    }
                """));
    }
}
