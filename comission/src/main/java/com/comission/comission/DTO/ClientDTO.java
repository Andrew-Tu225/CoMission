package com.comission.comission.DTO;

import com.comission.comission.client.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientDTO {
    private long id;
    private String username;
    private String businessName;
    private String email;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.username = client.getUsername();
        this.businessName = client.getBusinessName();
        this.email = client.getEmail();
    }
}
