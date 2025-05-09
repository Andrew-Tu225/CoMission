package com.comission.comission.model;

import com.comission.comission.project.Project;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="tags")
@NoArgsConstructor
public class Tag implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @ManyToMany(mappedBy = "tags")
    private Set<Project> relatedProjects = new HashSet<>();

    public Tag(String name)
    {
        this.name = "#"+name;
    }
}
