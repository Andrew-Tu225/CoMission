package com.comission.comission.project.resource;

import com.comission.comission.project.Project;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class ProjectResource {
    @Id
    private Long id; // This will be the same as the project id

    @OneToOne
    @MapsId // This tells JPA to use project.id as this entity's id
    @JoinColumn(name = "project_id") // Explicitly name the join column
    private Project project;

    public ProjectResource(Project project)
    {
        this.project=project;
    }
}
