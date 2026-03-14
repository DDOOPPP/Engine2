package org.gi.mMOEngine.manager;

import org.gi.Result;
import org.gi.mMOEngine.MMOEngine;
import org.gi.manager.IBuffManager;
import org.gi.manager.IModifierManager;
import org.gi.model.Buff;
import org.gi.model.Enums;
import org.gi.model.StatModifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class BuffManager implements IBuffManager {
    private Map<String,Buff> buffMap = new ConcurrentHashMap<>();
    private IModifierManager modifierManager;
    private final Logger logger;
    public BuffManager(IModifierManager manager){
        this.modifierManager = manager;
        logger = MMOEngine.getInstance().getLogger();
    }

    @Override
    public Result addBuff(Buff buff) {
        if (buff == null){
            return Result.ERROR("Buff is Null");
        }
        if (buffMap.containsKey(buff.getBuffId())){
            var modify = buffMap.get(buff.getBuffId());

            if (modify.isStack() && !modify.isMax()){
                modify = addStack(modify);
            }
            modify = reset(modify);

            buffMap.put(modify.getBuffId(),modify);
            return Result.SUCCESS;
        }
        buffMap.put(buff.getBuffId(),reset(buff));

        for (StatModifier modifier : buff.getModifierList()){
            modifierManager.add(modifier);
        }
        return Result.SUCCESS;
    }

    @Override
    public Result removeBuff(String buffId) {
        if (buffId == null || buffId.isEmpty()){
            return Result.ERROR("ID is Null Or Empty");
        }
        if (!buffMap.containsKey(buffId)){
            return Result.ERROR("Buff is Not Found");
        }
        var buff = buffMap.remove(buffId);

        for (StatModifier modifier : buff.getModifierList()){
            var result = modifierManager.remove(modifier.getModifierId());
            if (!result.isSuccess()){
                return Result.ERROR("modifier remove Failed");
            }
        }
        return Result.SUCCESS;
    }

    @Override
    public List<Buff> getAllBuff() {
        return buffMap.values().stream().toList();
    }

    @Override
    public List<Buff> findBySourceId(String sourceId) {
        return buffMap.values().stream().filter(
                buff -> buff.getSourceId().equals(sourceId)
        ).toList();
    }

    @Override
    public List<Buff> getBuffByType(Enums.BuffType type) {
        return buffMap.values().stream().filter(
                buff -> buff.getType().equals(type)
        ).toList();
    }

    @Override
    public Optional<Buff> findByID(String buffId) {
        if (buffId == null){
            return Optional.empty();
        }

        return Optional.ofNullable(buffMap.get(buffId));
    }

    @Override
    public Result tick() {
        for (var buff : buffMap.values()){
            if (!buff.isExpire()){
                continue;
            }

            var result = removeBuff(buff.getBuffId());

            if (!result.isSuccess()){
                logger.severe(result.getMessage());
            }
        }

        return Result.SUCCESS;
    }

    private Buff reset(Buff buff){
        buff.setExpiredTime(System.currentTimeMillis()+buff.getDuration());
        return buff;
    }

    private Buff addStack(Buff buff){
        buff.setCurrentStack(buff.getCurrentStack()+1);

        for (StatModifier modifier : buff.getModifierList()){
            modifierManager.add(modifier);
        }
        return buff;
    }
}
