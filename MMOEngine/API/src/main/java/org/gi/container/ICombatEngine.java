package org.gi.container;

import org.gi.model.DamageLog;
import org.gi.model.EntityStatData;

public interface ICombatEngine {
    public DamageLog runCombat(EntityStatData attacker, EntityStatData defender);
}
