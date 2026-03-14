package org.gi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.gi.model.Enums.SkillType;
import org.gi.model.Enums.DamageType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Skill {
    String skillId;
    String skillName;
    SkillType skillType;
    int maxLevel;
    DamageType damageType;
    List<SkillEffect> skillEffects;
    long cooldown;
    double manaCost;

    public boolean isActive(){
        return this.skillType.equals(SkillType.ACTIVE);
    }

    public boolean isMax(int currentLevel){
        return this.maxLevel <= currentLevel;
    }
}
