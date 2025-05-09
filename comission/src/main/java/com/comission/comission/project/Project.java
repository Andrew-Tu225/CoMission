package com.comission.comission.project;

import com.comission.comission.project.resource.ProjectFilesResource;
import com.comission.comission.project.resource.ProjectLinksResource;
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

    @ManyToMany(mappedBy = "projects")
    private List<User> members = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_tag",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonIgnore
    private List<Tag> tags = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "files_resource_id")
    private ProjectFilesResource filesResource;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "links_resource_id")
    private ProjectLinksResource linksResource;

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
