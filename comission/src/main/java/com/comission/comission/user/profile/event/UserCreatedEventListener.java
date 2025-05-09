package com.comission.comission.user.profile.event;

import com.comission.comission.user.User;
import com.comission.comission.user.profile.UserProfile;
import com.comission.comission.user.profile.UserProfileRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventListener {

    private final UserProfileRepository userProfileRepo;

    public UserCreatedEventListener(UserProfileRepository userProfileRepo)
    {
        this.userProfileRepo=userProfileRepo;
    }
    @EventListener
    public void handleUserCreated(UserCreatedEvent event)
    {
        User user = event.getUser();
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfileRepo.save(userProfile);
    }

}
