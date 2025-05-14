package com.comission.comission.user.event;

import com.comission.comission.user.Role;
import com.comission.comission.user.User;
import com.comission.comission.user.profile.UserProfile;
import com.comission.comission.user.profile.UserProfileRepository;
import com.comission.comission.user.profile.event.UserCreatedEvent;
import com.comission.comission.user.profile.event.UserCreatedEventListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserCreationEventListenerTest {
    @Mock
    private UserProfileRepository userProfileRepo;
    @InjectMocks
    private UserCreatedEventListener userCreatedEventListener;

    @Test
    void userProfileCreatedWhenUserCreatedEventReceivedTest()
    {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRoles(List.of(new Role("PLATFORM_USER")));

        UserCreatedEvent event = new UserCreatedEvent(testUser);
        userCreatedEventListener.handleUserCreated(event);

        verify(userProfileRepo).save(any(UserProfile.class));
    }
}
