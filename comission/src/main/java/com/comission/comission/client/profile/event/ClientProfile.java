package com.comission.comission.client.profile.event;

import com.comission.comission.client.Client;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="client-profiles")
public class ClientProfile {
    @Id
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name="client_id")
    private Client client;


}
