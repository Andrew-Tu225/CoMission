package com.comission.comission.controller;

import com.comission.comission.DTO.ProjectCreateRequest;
import com.comission.comission.project.Project;
import com.comission.comission.model.Tag;
import com.comission.comission.user.User;
import com.comission.comission.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("project")
public class ProjectController {
    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService)
    {
        this.projectService=projectService;
    }

    @PostMapping("/create")
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

    @GetMapping("/search")
    public List<Project> searchProject(@RequestParam("query") String query)
    {
        return projectService.getProjectFromLikeQuery(query);
    }

    @GetMapping("/{projectId}")
    public Project getProject(@PathVariable("projectId") long projectId)
    {
        Optional<Project> project = projectService.getProject(projectId);
        if(project.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "project not found");
        }
        else
        {
            return project.get();
        }
    }

    @PostMapping("/create-tag")
    public ResponseEntity<?> createTag(@RequestBody String tagName)
    {
        return projectService.createTag(tagName);
    }

    @PostMapping("/{projectId}/add-tags")
    public ResponseEntity<?> addTags(
            @PathVariable("projectId") long projectId,
            @RequestBody List<Tag> tags)
    {
        Optional<Project> project = projectService.getProject(projectId);
        if(project.isPresent())
        {
            return projectService.addTags(project.get(),tags);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"project is not found");
        }

    }


}
