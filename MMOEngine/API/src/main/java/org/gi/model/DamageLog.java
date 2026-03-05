package org.gi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
@AllArgsConstructor
public class DamageLog {
    private final UUID target;
    private final UUID attacker;
    private final double finalDamage;
    private final boolean isCritical;
    private final boolean isEvade;
    private final boolean isBlock;
}
