package org.gi.mMOEngine.register;

import org.gi.Result;
import org.gi.model.Setting;
import org.gi.model.Stat;
import org.gi.register.ICombatRegister;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CombatRegister implements ICombatRegister {
    private final Map<String, Setting> settingMap = new ConcurrentHashMap<>();
    private volatile boolean lock = false;

    @Override
    public Result register(Setting setting) {
        if (isLock()){
            return Result.ERROR("Combat Register is Locked");
        }
        if (setting == null){
            return Result.NULL;
        }
        settingMap.put(setting.getId(),setting);
        return Result.SUCCESS;
    }

    @Override
    public Result unRegister(String settingId) {
        if (isLock()){
            return Result.ERROR("Combat Register is Locked");
        }
        if (settingId == null || settingId.isEmpty()){
            return Result.ERROR("Setting is Null Or Empty");
        }
        var result = settingMap.remove(settingId);
        if (result == null){
            return Result.ERROR("%s is Not found Stat Remove Failed".formatted(settingId));
        }
        return Result.SUCCESS;
    }

    @Override
    public Optional<Setting> findByName(String settingId) {
        if (settingId == null || settingId.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(settingMap.get(settingId));
    }

    @Override
    public Map<String, Setting> getAllSetting() {
        return Map.copyOf(settingMap);
    }

    @Override
    public boolean containSetting(String settingId) {
        return settingMap.containsKey(settingId);
    }

    @Override
    public Result clear() {
        if (isLock()){
            return Result.ERROR("Combat Register is Locked");
        }
        settingMap.clear();
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
