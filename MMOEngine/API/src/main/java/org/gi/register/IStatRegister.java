package org.gi.register;

import org.gi.Result;
import org.gi.model.Enums.StatCategory;
import org.gi.model.Stat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IStatRegister {
    public Result register(Stat stat);

    public Result unRegister(String statId);

    public Optional<Stat> findByName(String statId);

    public List<Stat> findByCategory(StatCategory category);

    public Collection<Stat> getAllStat();

    public Result clear();

    public boolean isLock();

    public void unlock();

    public void lock();
}
