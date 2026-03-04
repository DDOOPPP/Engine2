package org.gi.mMOEngine.register;

import org.gi.Result;
import org.gi.model.Enums.StatCategory;
import org.gi.model.Stat;
import org.gi.register.IStatRegister;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StatRegister implements IStatRegister {
    private final Map<String,Stat> statMap = new ConcurrentHashMap<>();
    private volatile boolean lock = false;

    @Override
    public Result register(Stat stat) {
        if (isLock()){
            return Result.ERROR("Stat Register is Locked");
        }
        if (stat == null){
            return Result.NULL;
        }
        statMap.put(stat.getId(),stat);
        return Result.SUCCESS;
    }

    @Override
    public Result unRegister(String statId) {
        if (isLock()){
            return Result.ERROR("Stat Register is Locked");
        }
        if (statId == null || statId.isEmpty()){
            return Result.ERROR("StatId is Null Or Empty");
        }
        var result = statMap.remove(statId);
        if (result == null){
            return Result.ERROR("%s is Not found Stat Remove Failed".formatted(statId));
        }
        return Result.SUCCESS;
    }

    @Override
    public Optional<Stat> findByName(String statId) {
        if (statId == null || statId.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(statMap.get(statId));
    }

    @Override
    public List<Stat> findByCategory(StatCategory category) {
        return statMap.values().stream()
                .filter(stat -> stat.getCategory().equals(category)).toList();
    }

    @Override
    public Collection<Stat> getAllStat() {
        return statMap.values();
    }

    @Override
    public boolean containStat(String statId) {
        return statMap.containsKey(statId);
    }

    @Override
    public Result clear() {
        if (isLock()){
            return Result.ERROR("Stat Register is Locked");
        }
        statMap.clear();
        return Result.SUCCESS;
    }

    @Override
    public boolean isLock() {
        return lock;
    }

    @Override
    public void unlock() {
        lock = false;
    }

    @Override
    public void lock() {
        lock = true;
    }
}
