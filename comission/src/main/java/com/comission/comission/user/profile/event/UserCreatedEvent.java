package com.comission.comission.user.profile.event;

import com.comission.comission.user.User;
import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {

    private final User user;

    public UserCreatedEvent(User user) {
        super(user);
        this.user=user;
    }

    public User getUser() {
        return user;
    }
}
