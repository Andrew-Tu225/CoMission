package com.comission.comission.DTO;

import com.comission.comission.project.Project;
import com.comission.comission.skill.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProjectDTO {
    private long id;
    private String title;
    private String description;
    private List<Skill> skills;
    private ClientDTO owner;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.skills = project.getSkills();
        this.owner = new ClientDTO(project.getOwner());
    }
}
