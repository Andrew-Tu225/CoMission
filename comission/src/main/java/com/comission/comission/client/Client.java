package com.comission.comission.client;

import com.comission.comission.common.AppUser;
import com.comission.comission.project.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="clients")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client extends AppUser {
    private String businessName;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Project> projects = new ArrayList<>();

    public Client(String email,String password, String businessName)
    {
        super(email, password);
        this.businessName=businessName;
    }
}
