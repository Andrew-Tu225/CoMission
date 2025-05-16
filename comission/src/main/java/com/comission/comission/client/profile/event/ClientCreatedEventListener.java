package com.comission.comission.client.profile.event;

import com.comission.comission.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ClientCreatedEventListener {
    private final ClientProfileRepository clientProfileRepo;

    @Autowired
    public ClientCreatedEventListener(ClientProfileRepository clientRepo)
    {
        this.clientProfileRepo=clientRepo;
    }

    @EventListener
    public void handleClientCreated(ClientCreatedEvent event)
    {
        Client client = event.getClient();
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setClient(client);
        clientProfileRepo.save(clientProfile);
    }
}
