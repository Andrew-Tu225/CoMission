package com.comission.comission.auth;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserRepository userRepo;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private ApplicationEventPublisher eventPublisher;


    private void setupRegisterUserMocks(User testUser) {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(testUser);
        doNothing().when(eventPublisher).publishEvent(any(UserCreatedEvent.class));
    }

    @Test
    void registerShouldCreateNewUserTest()
    {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRoles(List.of(new Role("PLATFORM_USER")));

        setupRegisterUserMocks(testUser);
        User registeredUser = authServiceImpl.register(testUser);

        verify(userRepo).save(testUser);
        verify(passwordEncoder).encode(anyString());
        assertEquals("encodedPassword", testUser.getPassword());
    }

    @Test
    void UserProfileCreatedAfterUserRegisteredTest()
    {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRoles(List.of(new Role("PLATFORM_USER")));

        authServiceImpl.publishUserCreatedEvent(testUser);

        ArgumentCaptor<UserCreatedEvent> eventCaptor = ArgumentCaptor.forClass(UserCreatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        UserCreatedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(testUser, capturedEvent.getUser());

    }
}
