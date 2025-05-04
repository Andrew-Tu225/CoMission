package com.comission.comission.model.resource;

import com.comission.comission.model.Project;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_links_resources")
public class ProjectLinksResource extends ProjectResource {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "links_resource_id")
    private List<LinkEntry> linkEntries = new ArrayList<>();

    // Required default constructor for JPA
    public ProjectLinksResource() {
        super();
    }

    public ProjectLinksResource(Project project) {
        super(project);
    }

    // Getters and setters (don't use @Data)
    public List<LinkEntry> getLinkEntries() {
        return linkEntries;
    }

    public void setLinkEntries(List<LinkEntry> linkEntries) {
        this.linkEntries = linkEntries;
    }
}