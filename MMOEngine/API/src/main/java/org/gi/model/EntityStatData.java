package org.gi.model;

import lombok.Getter;
import org.gi.container.IStatContainer;
import org.gi.manager.IBuffManager;
import org.gi.manager.IModifierManager;
import org.gi.math.IStatCalculator;

import java.util.UUID;

@Getter

public class EntityStatData {
    private final UUID uuid;
    private final IModifierManager manager;
    private final IBuffManager buffManager;
    private final IStatContainer container;
    private final IStatCalculator calculator;

    public EntityStatData(UUID uuid,IBuffManager buffManager, IModifierManager manager, IStatContainer container, IStatCalculator calculator) {
        this.uuid = uuid;
        this.manager = manager;
        this.buffManager = buffManager;
        this.container = container;
        this.calculator = calculator;
    }
}