package org.gi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.gi.model.Enums.StatCategory;

@Getter
@AllArgsConstructor
@ToString
public class Stat {
    private String id;
    private String display;
    private StatCategory category;
    private double defaultValue;
    private double minValue;
    private double maxValue;
}
