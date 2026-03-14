package org.gi.model;

public class Enums {
    public enum StatCategory{
        OFFENSE,DEFENSE,UTILITY
    }
    public enum ModifierSource{
        BASE, EQUIPMENT
    }
    public enum Operator{
        FLAT, PERCENT
    }

    public enum DamageType {
        PHYSICAL, MAGICAL
    }

    public enum BuffType{
        BUFF, DEBUFF
    }

    public enum SkillType{
        ACTIVE,PASSIVE
    }

    public enum EffectType{
        DAMAGE, HEAL, BUFF, DEBUFF
    }

    public enum Target{
        SELF, SINGLE , MULTI
    }
}
