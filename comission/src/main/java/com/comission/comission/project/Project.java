package com.comission.comission.project;

import com.comission.comission.client.Client;
import com.comission.comission.skill.Skill;
import com.comission.comission.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="projects")
@NoArgsConstructor
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonIgnore
    private List<Skill> skills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client owner;
    @ManyToOne(fetch = FetchType.LAZY)
    private User freelancer;

    private LocalDate endDate = null;
    private boolean complete = false;

    public Project(String title, String description, LocalDate endDate)
    {
        this.title=title;
        this.description=description;
        this.endDate=endDate;
    }

    public Project(String title, String description)
    {
        this.title=title;
        this.description=description;
    }
}
