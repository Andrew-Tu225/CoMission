package com.comission.comission.project;

import com.comission.comission.DTO.ProjectDTO;
import com.comission.comission.client.Client;
import com.comission.comission.common.AppUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    @Autowired
    public ProjectService(ProjectRepository projectRepo)
    {
        this.projectRepo=projectRepo;
    }

    public List<ProjectDTO> getProjectFromLikeQuery(String query)
    {
        List<Project> projects = projectRepo.getProjectByLikeQuery(query);
        List<ProjectDTO> results = projects.stream().map(ProjectDTO::new).toList();
        return results;
    }

    public Project createProject(Project project)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client projectOwner = (Client) authentication.getPrincipal();
        System.out.println(projectOwner.getUsername());
        project.setOwner(projectOwner);
        return projectRepo.save(project);
    }

    public Project getProject(long projectId)
    {
        Optional<Project> project = projectRepo.findById(projectId);
        if(project.isPresent())
        {
            return project.get();
        }
        else
        {
            throw new EntityNotFoundException("project not found");
        }
    }

    public boolean isAuthorizedForProject(long projectId)
    {
        Project project = getProject(projectId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = (AppUser) authentication.getPrincipal();
        return project.getOwner().getUsername().equals(appUser.getUsername());
    }

    public void updateProject(long projectId, Project updatedProject)
    {
        updatedProject.setId(projectId);
        projectRepo.save(updatedProject);
    }

    public Project saveProject(Project project)
    {
        return projectRepo.save(project);
    }
//    @Transactional
//    public ResponseEntity<?> addSkills(Project project, List<Skill> skills)
//    {
//        Set<Skill> currentSkills = new HashSet<>(project.getSkills());
//        currentSkills.addAll(skills);
//        for(Skill skill : skills)
//        {
//            skill.getRelatedProjects().add(project);
//        }
//        project.setSkills(new ArrayList<>(currentSkills));
//        return ResponseEntity.ok("skills added successfully");
//    }
//
//    public ResponseEntity<?> createSkill(String skillName)
//    {
//        if(skillRepo.getByName(skillName).isEmpty())
//        {
//            Skill newSkill = new Skill(skillName);
//            skillRepo.save(newSkill);
//            return ResponseEntity.ok("skill creates successfully");
//        }
//        else
//        {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("skill name already exist");
//        }
//    }
}
