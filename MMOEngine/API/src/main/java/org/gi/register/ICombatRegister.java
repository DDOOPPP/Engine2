package org.gi.register;

import org.gi.Result;
import org.gi.model.Setting;

import java.util.*;

public interface ICombatRegister {
    public Result register(Setting setting);

    public Result unRegister(String settingId);

    public Optional<Setting>  findByName(String settingId);

    public Map<String, Setting> getAllSetting();

    public boolean containSetting(String settingId);

    public Result clear();

    public boolean isLock();

    public void unlock();

    public void lock();
}
