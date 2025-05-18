package com.comission.comission.user;

import com.comission.comission.common.AppUser;
import com.comission.comission.project.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
public class User extends AppUser {
    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "freelancer")
    @JsonIgnore
    private List<Project> projects = new ArrayList<>();

    public User(String firstName,String lastName, String email,String password)
    {
        super(email,password);
        this.firstName=firstName;
        this.lastName=lastName;
    }
}
