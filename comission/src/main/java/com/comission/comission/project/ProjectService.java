package com.comission.comission.project;

import com.comission.comission.DTO.ProjectDTO;
import com.comission.comission.DTO.SkillDTO;
import com.comission.comission.client.Client;
import com.comission.comission.common.AppUser;
import com.comission.comission.skill.Skill;
import com.comission.comission.skill.SkillRepository;
import com.comission.comission.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final SkillRepository skillRepo;
    @Autowired
    public ProjectService(ProjectRepository projectRepo, SkillRepository skillRepo)
    {
        this.projectRepo=projectRepo;
        this.skillRepo=skillRepo;
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

    @Transactional
    public ResponseEntity<?> addSkills(Project project, List<SkillDTO> skills)
    {
        Set<Skill> currentSkills = new HashSet<>(project.getSkills());
        for(SkillDTO skillDTO : skills)
        {
            Skill skill = skillRepo.findById(skillDTO.getId());
            skill.getRelatedProjects().add(project);
            currentSkills.add(skill);
        }
        project.setSkills(new ArrayList<>(currentSkills));
        return ResponseEntity.ok("skills added successfully");
    }

    public Project addUserToProject(Project project, User user)
    {
        if (!isAuthorizedForProject(project.getId())) {
            throw new IllegalStateException("You don't have credentials to modify this project");
        }

        if (project.getFreelancer() != null) {
            throw new IllegalStateException("User already exists in this project");
        }

        project.setFreelancer(user);
        projectRepo.save(project);
        return project;
    }
}
