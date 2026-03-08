package org.gi.container;

import org.gi.model.DamageLog;
import org.gi.model.EntityStatData;
import org.gi.model.Enums.DamageType;

public interface ICombatEngine {
    public DamageLog runCombat(EntityStatData attacker, EntityStatData defender, DamageType type);
}
