package com.comission.comission.skill;

import com.comission.comission.DTO.SkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {
    private final SkillRepository skillRepo;

    @Autowired
    public SkillService(SkillRepository skillRepo)
    {
        this.skillRepo=skillRepo;
    }

    public Skill createSkill(Skill skill)
    {
        return skillRepo.save(skill);
    }

    public List<SkillDTO> searchSkills(String query)
    {
        List<Skill> relatedSkills = skillRepo.findByNameContainingIgnoreCase(query);
        return relatedSkills.stream().map(SkillDTO::new).toList();
    }
}
