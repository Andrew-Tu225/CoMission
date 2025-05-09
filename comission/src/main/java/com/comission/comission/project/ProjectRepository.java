package com.comission.comission.project;

import com.comission.comission.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("select members from Project p JOIN p.members members where p.id=:id")
    List<User> getProjectUsers(@Param("id") Long id);

    public default List<Project> getProjectByLikeQuery(String query)
    {
        query = query.replace("\"", "").trim();
        String[] words = query.split(("\\s+"));
        Set<Project> results = new HashSet<>();
        for(String word : words)
        {
            if(word.length() < 3)
                continue;

            Set<Project> projectsWithQuery = new HashSet<>(findByTitleContainingIgnoreCase(word));
            results.addAll(projectsWithQuery);
        }

        return new ArrayList<>(results);
    }

    Optional<Project> findById(long id);

    Set<Project> findByTitleContainingIgnoreCase(String word);



}
