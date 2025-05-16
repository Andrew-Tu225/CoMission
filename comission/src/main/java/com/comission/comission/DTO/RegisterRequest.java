package com.comission.comission.DTO;

import com.comission.comission.client.Client;
import com.comission.comission.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String registerType; // "USER" or "CLIENT"

    public RegisterRequest(String username, String email, String password, String registerType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registerType = registerType;
    }
}
