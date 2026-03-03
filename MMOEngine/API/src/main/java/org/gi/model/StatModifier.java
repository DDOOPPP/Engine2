package org.gi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.gi.model.Enums.ModifierSource;
import org.gi.model.Enums.Operator;

@Getter
@AllArgsConstructor
@ToString
public class StatModifier {
    private String modifierId;
    private String sourceId;
    private String statId;
    private Operator operator;
    private ModifierSource source;
    private double value;
}
