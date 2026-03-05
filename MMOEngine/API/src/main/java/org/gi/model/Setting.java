package org.gi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Setting {
    private String id;
    private String attackerStatId;
    private String defenderStatId;
    private double maxLimit;
    private double minLimit;
}
