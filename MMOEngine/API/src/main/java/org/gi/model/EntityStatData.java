package org.gi.model;

import lombok.Getter;
import org.gi.container.IStatContainer;
import org.gi.manager.IModifierManager;
import org.gi.math.IStatCalculator;

import java.util.UUID;

@Getter

public class EntityStatData {
    private final UUID uuid;
    private final IModifierManager manager;
    private final IStatContainer container;
    private final IStatCalculator calculator;

    public EntityStatData(UUID uuid, IModifierManager manager, IStatContainer container, IStatCalculator calculator) {
        this.uuid = uuid;
        this.manager = manager;
        this.container = container;
        this.calculator = calculator;
    }
}