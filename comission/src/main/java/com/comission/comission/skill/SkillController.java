package com.comission.comission.skill;

import com.comission.comission.DTO.SkillDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("skill")
public class SkillController {
    private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService)
    {
        this.skillService=skillService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSkill(@RequestBody Skill skill)
    {
        Skill newSkill = skillService.createSkill(skill);
        return ResponseEntity.ok(newSkill);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchSkill(@RequestBody String query)
    {
        List<SkillDTO> searchResults = skillService.searchSkills(query);
        return ResponseEntity.ok(searchResults);
    }

}
