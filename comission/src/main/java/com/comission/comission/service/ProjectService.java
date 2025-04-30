package com.comission.comission.service;

import com.comission.comission.model.Project;
import com.comission.comission.model.User;
import com.comission.comission.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final UserServiceImpl userServiceImpl;
    @Autowired
    public ProjectService(ProjectRepository projectRepo, UserServiceImpl userServiceImpl)
    {
        this.projectRepo=projectRepo;
        this.userServiceImpl=userServiceImpl;
    }

    public List<User> getAllProjectUsers(Long projectId)
    {
        return projectRepo.getProjectUsers(projectId);
    }

    public List<Project> getProjectFromLikeQuery(String query)
    {
        return projectRepo.getProjectByLikeQuery(query);
    }

    public Project createProject(Project project, List<String> membersUsername, User creator)
    {
        List<User> members = new ArrayList<>();
        members.add(creator);
        for(String username:membersUsername)
        {
            User user = (User)userServiceImpl.loadUserByUsername(username);
            userServiceImpl.addProject(user, project);
            members.add(user);
        }
        project.setMembers(members);
        return projectRepo.save(project);
    }
}
