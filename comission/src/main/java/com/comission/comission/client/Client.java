package com.comission.comission.client;

import com.comission.comission.common.AppUser;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name="clients")
@NoArgsConstructor
public class Client extends AppUser {
    private String businessName;

    public Client(String email,String password, String businessName)
    {
        super(email, password);
        this.businessName=businessName;
    }
}
