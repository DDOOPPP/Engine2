package org.gi.mMOEngine.manager;

import org.gi.Result;
import org.gi.mMOEngine.MMOEngine;
import org.gi.manager.IModifierManager;
import org.gi.model.Enums.ModifierSource;
import org.gi.model.StatModifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ModifierManager implements IModifierManager {
    private Logger logger = MMOEngine.getInstance().getLogger();
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
        if (modifierId == null || modifierId.isEmpty()){
            return Result.ERROR("ModifierId is Null");
        }
        StatModifier modifier = modifierMap.get(modifierId);

        if (modifier == null){
            logger.warning("Modifier is Null");
            return Result.ERROR("Modifier is Null");
        }

        removeByStatId(modifier);
        removeBySourceId(modifier);

        modifierMap.remove(modifier.getModifierId());
        return Result.SUCCESS;
    }

    @Override
    public Result removeBySource(String sourceId) {
        var list = modifierBySourceId.get(sourceId);

        if (list == null || list.isEmpty()){
            return Result.ERROR("SourceIdList is Null Or Empty");
        }

        list.forEach((modifier) ->{
            modifierMap.remove(modifier.getModifierId());
            removeByStatId(modifier);
        });
        modifierBySourceId.remove(sourceId);
        return Result.SUCCESS;
    }

    @Override
    public Result removeAllBySource(ModifierSource source) {
        //관련 SourceType 전체 제거 아직은 미사용

        return null;
    }

    @Override
    public Optional<StatModifier> getModifier(String modifierId) {
        if (modifierId == null || modifierId.isEmpty()){
            return Optional.empty();
        }

        return Optional.ofNullable(modifierMap.get(modifierId));
    }

    @Override
    public List<StatModifier> findBySourceId(String sourceId) {
        if (sourceId == null || sourceId.isEmpty()){
            return List.of();
        }
        if (!modifierBySourceId.containsKey(sourceId)){
            return List.of();
        }

        return modifierBySourceId.get(sourceId);
    }

    @Override
    public List<StatModifier> findBySource(ModifierSource source) {
        if (source == null){
            return List.of();
        }

        return modifierMap.values().stream()
                .filter(modifier -> modifier.getSource().equals(source)).toList();
    }

    @Override
    public List<StatModifier> findByStatId(String statId) {
        if (statId == null || statId.isEmpty()){
            return List.of();
        }
        if (!modifierByStatId.containsKey(statId)){
            return List.of();
        }

        return modifierByStatId.get(statId);
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

    private void removeByStatId(StatModifier modifier){
        var list = modifierByStatId.get(modifier.getStatId());

        if (list == null){
            logger.warning("Modifier StatId List is Null");
            return;
        }
        list.remove(modifier);

        if (list.isEmpty()){
            modifierByStatId.remove(modifier.getStatId());
        }
    }

    private void removeBySourceId(StatModifier modifier){
        var list = modifierBySourceId.get(modifier.getSourceId());

        if (list == null){
            logger.warning("Modifier SourceId List is Null");
            return;
        }
        list.remove(modifier);

        if (list.isEmpty()){
            modifierBySourceId.remove(modifier.getSourceId());
        }
    }
}
