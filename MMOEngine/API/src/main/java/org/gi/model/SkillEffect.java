package org.gi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.gi.model.Enums.EffectType;
import org.gi.model.Enums.Target;

@Getter
@Setter
@AllArgsConstructor
public class SkillEffect {
    private Target target;
    private EffectType effectType;
    private double baseValue;
    private double valuePerLevel;
    private long duration; //BUFF지속 시간
    private String buffId; //BUFF의 경우 ID
}
