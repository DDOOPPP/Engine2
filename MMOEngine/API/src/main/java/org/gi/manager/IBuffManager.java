package org.gi.manager;

import org.gi.Result;
import org.gi.model.Buff;
import org.gi.model.Enums;

import java.util.List;
import java.util.Optional;

public interface IBuffManager {
    public Result addBuff(Buff buff);

    public Result removeBuff(String buffId);

    public List<Buff> getAllBuff();

    public List<Buff> findBySourceId(String sourceId);

    public List<Buff> getBuffByType(Enums.BuffType type);

    public Optional<Buff> findByID(String buffId);

    public Result tick();
}
