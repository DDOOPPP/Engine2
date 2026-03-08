package org.gi.model;

import java.util.List;
import org.gi.model.Enums.BuffType;

public class Buff {
    private String buffId;
    private String sourceId;
    private BuffType type;
    private boolean isStack;
    private int currentStack;
    private int maxStack;
    private long duration;
    private long expiredTime;
    private List<StatModifier> modifierList;
}
