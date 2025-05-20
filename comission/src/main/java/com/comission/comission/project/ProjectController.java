package com.comission.comission.project;

import com.comission.comission.DTO.ProjectDTO;
import com.comission.comission.DTO.SkillDTO;
import com.comission.comission.common.AppUser;
import com.comission.comission.common.AppUserService;
import com.comission.comission.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("project")
public class ProjectController {
    private final ProjectService projectService;
    private final AppUserService appUserService;

    @Autowired
    public ProjectController(ProjectService projectService, AppUserService appUserService)
    {
        this.projectService=projectService;
        this.appUserService = appUserService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody Project project)
    {
        Project newProject = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProject);

    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProject(@RequestParam("query") String query)
    {
        List<ProjectDTO> searchResults = projectService.getProjectFromLikeQuery(query);
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProject(@PathVariable("projectId") long projectId)
    {
        Project project = projectService.getProject(projectId);
        return ResponseEntity.ok(project);
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<?> updateProject(
            @PathVariable("projectId") long projectId, @RequestBody Project updatedProject)
    {
        try
        {
            Project project = projectService.getProject(projectId);
            if(!projectService.isAuthorizedForProject(projectId))
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized");
            }
            projectService.updateProject(projectId, updatedProject);
            return ResponseEntity.ok("Project updated successfully");
        }
        catch(EntityNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e)
        {
            // fallback for unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error");
        }
    }


    @PostMapping("/{projectId}/add-skills")
    public ResponseEntity<?> addSkills(
            @PathVariable("projectId") long projectId,
            @RequestBody List<SkillDTO> skills)
    {
        try{
            Project project = projectService.getProject(projectId);
            return projectService.addSkills(project, skills);
        }
        catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-user/{username}")
    public ResponseEntity<?> addUserToProject(@PathVariable String username, @RequestBody long projectId)
    {
        try
        {
            Project project = projectService.getProject(projectId);
            AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);

            if(appUser.isUser())
            {
                User addedUser = (User) appUser;
                Project updatedProject = projectService.addUserToProject(project, addedUser);
                return ResponseEntity.ok(updatedProject);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("only freelancer allows to be added for this field");
            }
        }
        catch(EntityNotFoundException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(IllegalStateException e)
        {
            //handle projectOwner unmatch with current appUser
            if (e.getMessage().contains("you don't have credential to modify this project"))
            {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
            }
            // Handle projectFreelancer already existed error
            else if (e.getMessage().contains("user already exists in this project"))
            {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            else
            {
                throw e; // or handle unknown case
            }
        }
    }
}
