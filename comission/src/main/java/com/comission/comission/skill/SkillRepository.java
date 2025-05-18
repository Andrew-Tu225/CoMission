package com.comission.comission.skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    Skill findById(long id);
    List<Skill> findByNameContainingIgnoreCase(String query);
}
