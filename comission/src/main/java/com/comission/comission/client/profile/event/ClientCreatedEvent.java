package com.comission.comission.client.profile.event;

import com.comission.comission.client.Client;
import com.comission.comission.user.User;
import org.springframework.context.ApplicationEvent;

public class ClientCreatedEvent extends ApplicationEvent {
    private final Client client;

    public ClientCreatedEvent(Client client) {
        super(client);
        this.client=client;
    }

    public Client getClient() {
        return client;
    }
}
