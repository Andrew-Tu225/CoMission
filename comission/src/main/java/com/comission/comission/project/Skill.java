package com.comission.comission.project;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="skills")
@NoArgsConstructor
public class Skill implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ManyToMany(mappedBy = "skills")
    private Set<Project> relatedProjects = new HashSet<>();

    public Skill(String name)
    {
        this.name = "#"+name;
    }
}
