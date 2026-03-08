package org.gi.manager;

import org.gi.Result;
import org.gi.model.Buff;

public interface IBuffManager {
    public Result addBuff(Buff buff);

    public Result removeBuff(String buffId);
    public boolean isExpired(String buffId);
    public Result update(Buff buff);
}
