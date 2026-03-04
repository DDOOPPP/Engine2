package org.gi.manager;

import org.gi.Result;
import org.gi.model.Enums.ModifierSource;
import org.gi.model.StatModifier;

import java.util.List;
import java.util.Optional;

public interface IModifierManager {
    public Result add(StatModifier modifier);

    public Result remove(String modifierId);

    public Result removeBySource(String sourceId);

    public Result removeAllBySource(ModifierSource source);

    public Optional<StatModifier> getModifier(String modifierId);

    public List<StatModifier> findBySourceId(String sourceId);

    public List<StatModifier> findBySource(ModifierSource source);

    public List<StatModifier> findByStatId(String statId);
}
