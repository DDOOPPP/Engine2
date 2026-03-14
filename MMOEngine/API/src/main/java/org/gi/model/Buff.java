package org.gi.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.gi.model.Enums.BuffType;

@Getter
@AllArgsConstructor
public class Buff {
    private String buffId;
    private String sourceId;
    private BuffType type;
    private boolean isStack;
    @Setter
    private int currentStack;
    private int maxStack;
    private long duration;
    @Setter
    private long expiredTime;
    private List<StatModifier> modifierList;

    public boolean isMax(){
        return currentStack >= maxStack;
    }

    public boolean isExpire(){
        return System.currentTimeMillis() >= expiredTime;
    }
}
