package com.ecosystem.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomOauth2UserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    private CustomOauth2UserService service;
    private OAuth2User githubUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        githubUser = mock(OAuth2User.class);
        when(githubUser.getName()).thenReturn("1");
        when(githubUser.getAttribute("login")).thenReturn("tester");
        when(githubUser.getAttribute("avatar_url")).thenReturn("http://img");

        service = new CustomOauth2UserService(appUserRepository) {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest req) {
                appUserRepository.findById(githubUser.getName())
                        .orElseGet(() -> {
                            AppUser newUser = new AppUser("1", "tester", "http://img");
                            appUserRepository.save(newUser);
                            return newUser;
                        });
                return githubUser;
            }
        };
    }

    @Test
    void loadUser_createsUser_whenNotExists() {
        when(appUserRepository.findById("1")).thenReturn(Optional.empty());
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(inv -> inv.getArgument(0));

        OAuth2User returned = service.loadUser(mock(OAuth2UserRequest.class));

        verify(appUserRepository).save(any(AppUser.class));
        assertNotNull(returned);
        assertEquals("1", returned.getName());
    }

    @Test
    void loadUser_returnsExistingUser_whenFound() {
        when(appUserRepository.findById("1"))
                .thenReturn(Optional.of(new AppUser("1", "tester", "http://img")));

        OAuth2User returned = service.loadUser(mock(OAuth2UserRequest.class));

        verify(appUserRepository, never()).save(any(AppUser.class));
        assertNotNull(returned);
        assertEquals("1", returned.getName());
    }

    @Test
    void loadUser_throwsException_whenRepoFails() {
        when(appUserRepository.findById("1"))
                .thenThrow(new RuntimeException("DB down"));

        assertThrows(RuntimeException.class,
                () -> service.loadUser(mock(OAuth2UserRequest.class)));
    }
}
