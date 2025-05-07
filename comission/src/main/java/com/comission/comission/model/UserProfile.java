package com.comission.comission.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="user_profiles")
public class UserProfile{
    @Id
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name="user_id")
    private User user;

    private List<String> skills = new ArrayList<>();
    private String bio;


}
