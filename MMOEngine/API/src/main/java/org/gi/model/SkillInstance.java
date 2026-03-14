package org.gi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SkillInstance {
    private Skill skill;
    private int currentLevel;
    private long lastUse;
}
