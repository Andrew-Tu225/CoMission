package com.comission.comission.model.resource;

import com.comission.comission.model.Project;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "project_files_resources")
public class ProjectFilesResource extends ProjectResource {
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "files_resource_id")
    private List<FileEntry> fileEntries = new ArrayList<>();

    // Required default constructor for JPA
    public ProjectFilesResource() {
        super();
    }

    public ProjectFilesResource(Project project) {
        super(project);
    }

    // Getters and setters (don't use @Data)
    public List<FileEntry> getFileEntries() {
        return fileEntries;
    }

    public void setFileEntries(List<FileEntry> fileEntries) {
        this.fileEntries = fileEntries;
    }
}