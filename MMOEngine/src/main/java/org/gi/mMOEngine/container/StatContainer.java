package org.gi.mMOEngine.container;

import org.gi.container.IStatContainer;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatContainer implements IStatContainer {
    private final Map<String,Double> playerStatMap = new ConcurrentHashMap<>();

    @Override
    public double getFinalValue(String statId) {
        if (!playerStatMap.containsKey(statId)){
            return 0;
        }
        return playerStatMap.get(statId);
    }

    @Override
    public Map<String, Double> getAllStat() {
        return Collections.unmodifiableMap(playerStatMap);
    }

    @Override
    public void notifyStatValue(String statId, double newValue) {
        playerStatMap.put(statId,newValue);
    }
}
