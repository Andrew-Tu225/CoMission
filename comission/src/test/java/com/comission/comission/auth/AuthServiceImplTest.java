package com.comission.comission.auth;

import com.comission.comission.DTO.RegisterRequest;
import com.comission.comission.client.Client;
import com.comission.comission.client.profile.event.ClientCreatedEvent;
import com.comission.comission.common.AppUser;
import com.comission.comission.common.AppUserRepository;
import com.comission.comission.user.Role;
import com.comission.comission.user.User;
import com.comission.comission.user.UserRepository;
import com.comission.comission.user.profile.event.UserCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AppUserRepository appUserRepo;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private ApplicationEventPublisher eventPublisher;


    private void setupRegisterUserMocks() {
        doNothing().when(eventPublisher).publishEvent(any(ClientCreatedEvent.class));
        doNothing().when(eventPublisher).publishEvent(any(UserCreatedEvent.class));
    }

    @Test
    void registerTestAsUser()
    {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test@123");
        request.setEmail("test@123gmail.com");
        request.setPassword("password");
        request.setRegisterType("USER");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(eventPublisher).publishEvent(any(UserCreatedEvent.class));
        when(appUserRepo.save(any(User.class))).thenReturn(any(User.class));
        AppUser registeredUser = authServiceImpl.register(request);

        verify(appUserRepo).save(any(User.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void registerTestAsClient()
    {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test@123");
        request.setEmail("test@123gmail.com");
        request.setPassword("password");
        request.setRegisterType("CLIENT");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(eventPublisher).publishEvent(any(ClientCreatedEvent.class));
        when(appUserRepo.save(any(Client.class))).thenReturn(any(Client.class));
        AppUser registeredClient = authServiceImpl.register(request);

        verify(appUserRepo).save(any(Client.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void UserProfileCreatedAfterUserRegisteredTest()
    {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRoles(List.of(new Role("PLATFORM_USER")));
        testUser.setUser(true);

        authServiceImpl.publishUserCreatedEvent(testUser);

        ArgumentCaptor<UserCreatedEvent> eventCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        UserCreatedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(testUser, capturedEvent.getUser());

    }

    @Test
    void ClientProfileCreatedAfterClientRegisteredTest()
    {
        Client testClient = new Client();
        testClient.setUsername("testuser");
        testClient.setPassword("password");
        testClient.setRoles(List.of(new Role("PLATFORM_USER")));
        testClient.setClient(true);

        authServiceImpl.publishUserCreatedEvent(testClient);

        ArgumentCaptor<ClientCreatedEvent> eventCaptor = ArgumentCaptor.forClass(ClientCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        ClientCreatedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(testClient, capturedEvent.getClient());
    }
}
