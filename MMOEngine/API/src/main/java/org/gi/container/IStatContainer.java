package org.gi.container;

import java.util.Map;

public interface IStatContainer {
    public double getFinalValue(String statId);

    public Map<String,Double> getAllStat();

    public void notifyStatValue(String statId,double newValue);
}
