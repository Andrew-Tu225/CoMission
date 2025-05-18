package com.comission.comission.DTO;

import com.comission.comission.skill.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillDTO
{
    private long id;
    private String name;

    public SkillDTO(Skill skill)
    {
        this.id=skill.getId();
        this.name=skill.getName();
    }
}
