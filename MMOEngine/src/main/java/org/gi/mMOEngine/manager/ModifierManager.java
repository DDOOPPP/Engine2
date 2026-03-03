package org.gi.mMOEngine.manager;

import org.gi.Result;
import org.gi.manager.IModifierManager;
import org.gi.model.Enums;
import org.gi.model.StatModifier;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModifierManager implements IModifierManager {
    private final Map<String,StatModifier> modifierMap = new ConcurrentHashMap<>();
    private final Map<String,List<StatModifier>> modifierByStatId = new ConcurrentHashMap<>();
    private final Map<String,List<StatModifier>> modifierBySourceId = new ConcurrentHashMap<>();

    @Override
    public Result add(StatModifier modifier) {
        if (modifier == null){
            return Result.ERROR("Modifier is Null");
        }
        modifierMap.put(modifier.getModifierId(),modifier);
        addByStatId(modifier);
        addBySourceId(modifier);
        return Result.SUCCESS;
    }

    @Override
    public Result remove(String modifierId) {
        return null;
    }

    @Override
    public Result removeBySource(String sourceId) {
        return null;
    }

    @Override
    public Result removeAllBySource(Enums.ModifierSource source) {
        return null;
    }

    @Override
    public StatModifier getModifier(String modifierId) {
        return null;
    }

    @Override
    public List<StatModifier> findBySourceId(String sourceId) {
        return List.of();
    }

    @Override
    public List<StatModifier> findBySource(Enums.ModifierSource source) {
        return List.of();
    }

    @Override
    public List<StatModifier> findByStatId(String statId) {
        return List.of();
    }

    private void addByStatId(StatModifier modifier){
        List<StatModifier> list =  modifierByStatId.computeIfAbsent(modifier.getStatId(), k -> new ArrayList<>());

        list.add(modifier);

        //modifierByStatId.put(modifier.getStatId(),list);
    }

    private void addBySourceId(StatModifier modifier){
        List<StatModifier> list =  modifierBySourceId.computeIfAbsent(modifier.getSourceId(), k -> new ArrayList<>());

        list.add(modifier);

        //modifierBySourceId.put(modifier.getSourceId(),list);
    }
}
