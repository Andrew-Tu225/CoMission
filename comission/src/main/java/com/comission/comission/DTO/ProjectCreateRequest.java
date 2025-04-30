package com.comission.comission.DTO;

import com.comission.comission.model.Project;
import lombok.Data;

import java.util.List;

@Data
public class ProjectCreateRequest {
    private Project project;
    private List<String> membersUsername;

    public ProjectCreateRequest(Project project,List<String> membersUsername)
    {
        this.project=project;
        this.membersUsername=membersUsername;
    }
}
