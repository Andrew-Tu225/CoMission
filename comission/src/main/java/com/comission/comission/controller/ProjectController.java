package com.comission.comission.controller;

import com.comission.comission.DTO.ProjectCreateRequest;
import com.comission.comission.model.Project;
import com.comission.comission.model.User;
import com.comission.comission.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService)
    {
        this.projectService=projectService;
    }

    @PostMapping("/project/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectCreateRequest projectCreateRequest)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User creator = (User) authentication.getPrincipal();
        Project createdProject = projectService.createProject(
                projectCreateRequest.getProject(),
                projectCreateRequest.getMembersUsername(),
                creator);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);

    }

    @GetMapping("/projects")
    public List<Project> searchProject(@RequestParam("query") String query)
    {
        return projectService.getProjectFromLikeQuery(query);
    }
}
