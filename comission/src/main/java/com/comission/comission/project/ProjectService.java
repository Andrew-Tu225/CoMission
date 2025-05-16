package com.comission.comission.project;

import com.comission.comission.user.User;
import com.comission.comission.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final TagRepository tagRepo;
    @Autowired
    public ProjectService(ProjectRepository projectRepo, TagRepository tagRepo)
    {
        this.projectRepo=projectRepo;
        this.tagRepo=tagRepo;
    }

    public List<User> getAllProjectUsers(Long projectId)
    {
        return projectRepo.getProjectUsers(projectId);
    }

    public List<Project> getProjectFromLikeQuery(String query)
    {
        return projectRepo.getProjectByLikeQuery(query);
    }

//    public Project createProject(Project project, List<String> membersUsername, User creator)
//    {
//        List<User> members = new ArrayList<>();
//        members.add(creator);
//        for(String username:membersUsername)
//        {
//            User user = (User)userServiceImpl.loadUserByUsername(username);
//            userServiceImpl.addProject(user, project);
//            members.add(user);
//        }
//        project.setMembers(members);
//        return projectRepo.save(project);
//    }

    public Optional<Project> getProject(long projectId)
    {
        return projectRepo.findById(projectId);
    }

    @Transactional
    public ResponseEntity<?> addTags(Project project, List<Tag> tags)
    {
        Set<Tag> currentTags = new HashSet<>(project.getTags());
        currentTags.addAll(tags);
        for(Tag tag:tags)
        {
            tag.getRelatedProjects().add(project);
        }
        project.setTags(new ArrayList<>(currentTags));
        return ResponseEntity.ok("tags added successfully");
    }

    public ResponseEntity<?> createTag(String tagName)
    {
        if(tagRepo.getByName(tagName).isEmpty())
        {
            Tag newTag = new Tag(tagName);
            tagRepo.save(newTag);
            return ResponseEntity.ok("tag creates successfully");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("tag name already exist");
        }
    }
}
