package com.comission.comission.model;

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

    @ManyToMany(mappedBy = "projects")
    private List<User> members = new ArrayList<>();

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
