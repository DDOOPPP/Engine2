package org.gi.mMOEngine.container;

import org.gi.container.ICombatEngine;
import org.gi.container.IStatContainer;
import org.gi.mMOEngine.loader.CombatLoader;
import org.gi.math.IDamageCalculator;
import org.gi.math.IRateCalculator;
import org.gi.model.*;
import org.gi.register.ICombatRegister;

public class CombatEngine implements ICombatEngine {
    private final IRateCalculator rateCalculator;
    private final IDamageCalculator damageCalculator;
    private final ICombatRegister register;
    private final Double defenseConstant;

    private final Setting evasionSetting;
    private final Setting chanceSetting;
    private final Setting multiplier;
    private final Setting block;
    private final Setting blockReduction;
    private final Setting physical;
    private final Setting magical;

    public CombatEngine(IRateCalculator rateCalculator,IDamageCalculator damageCalculator,
                        ICombatRegister register){
        this.defenseConstant = 100.00;
        this.rateCalculator =rateCalculator;
        this.register = register;
        this.damageCalculator =damageCalculator;

        evasionSetting = register.findByName(StatName.EVASION).get();
        chanceSetting = register.findByName(StatName.CHANCE).get();
        multiplier = register.findByName(StatName.MULTIPLIER).get();
        block = register.findByName(StatName.BLOCK).get();
        blockReduction = register.findByName(StatName.BLOCK_REDUCTION).get();
        physical = register.findByName(Enums.DamageType.PHYSICAL.name().toLowerCase()).get();
        magical = register.findByName(Enums.DamageType.MAGICAL.name().toLowerCase()).get();
    }

    @Override
    public DamageLog runCombat(EntityStatData attacker, EntityStatData defender, Enums.DamageType type) {
        var A_CONTAINER = attacker.getContainer();
        var D_CONTAINER = defender.getContainer();

        String damageStatId = "";
        String defenseStatId = "";
        double finalDamage = 0;
        boolean isCritical = false;
        boolean isBlock = false;
        boolean evasion = checkEvade(A_CONTAINER,D_CONTAINER);
        if (evasion){
            return new DamageLog(
                    defender.getUuid(),
                    attacker.getUuid(),
                    finalDamage,
                    isCritical,
                    evasion,
                    isBlock
            );
        }

        switch (type){
            case MAGICAL : {
                damageStatId = magical.getAttackerStatId();
                defenseStatId = magical.getDefenderStatId();
            }
            break;
            case PHYSICAL: {
                damageStatId = physical.getAttackerStatId();
                defenseStatId = physical.getDefenderStatId();
            }
            break;
        }

        finalDamage = damageCalculator.calculateDamage(A_CONTAINER.getFinalValue(damageStatId),D_CONTAINER.getFinalValue(defenseStatId),defenseConstant);

        isCritical = checkCritical(A_CONTAINER,D_CONTAINER);
        if (isCritical){
            double rate = calculateMultiplier(A_CONTAINER,D_CONTAINER);

            finalDamage = damageCalculator.calculateCritical(finalDamage,rate);
        }

        isBlock = checkBlock(A_CONTAINER,D_CONTAINER);

        if (isBlock){
            double rate = calculateBlockRate(A_CONTAINER,D_CONTAINER);

            finalDamage = damageCalculator.calculateBlock(finalDamage,rate);
        }

        return new DamageLog(defender.getUuid(),attacker.getUuid(),finalDamage,isCritical,false,isBlock);
    }

    private boolean checkEvade(IStatContainer attacker, IStatContainer defender){
        double accuracy = attacker.getFinalValue(evasionSetting.getAttackerStatId());
        double evasion = defender.getFinalValue(evasionSetting.getDefenderStatId());

        return rateCalculator.success(rateCalculator.calculate(accuracy,evasion,evasionSetting.getMaxLimit(),evasionSetting.getMinLimit()));
    }

    private boolean checkBlock(IStatContainer attacker, IStatContainer defender){
        double A_VALUE = attacker.getFinalValue(block.getAttackerStatId());
        double D_VALUE = defender.getFinalValue(block.getDefenderStatId());

        return rateCalculator.success(rateCalculator.calculate(A_VALUE,D_VALUE,block.getMaxLimit(), block.getMinLimit()));
    }

    private boolean checkCritical(IStatContainer attacker, IStatContainer defender){
        double chance = attacker.getFinalValue(chanceSetting.getAttackerStatId());
        double resist = defender.getFinalValue(chanceSetting.getDefenderStatId());

        return rateCalculator.success(rateCalculator.calculate(chance,resist,chanceSetting.getMaxLimit(),chanceSetting.getMinLimit()));
    }

    private double calculateBlockRate(IStatContainer attacker, IStatContainer defender) {
        double A_VALUE = attacker.getFinalValue(blockReduction.getAttackerStatId());
        double D_VALUE = defender.getFinalValue(blockReduction.getDefenderStatId());

        return rateCalculator.calculate(A_VALUE,D_VALUE,blockReduction.getMaxLimit(), blockReduction.getMinLimit());
    }

    private double calculateMultiplier(IStatContainer attacker, IStatContainer defender) {
        double A_VALUE = attacker.getFinalValue(multiplier.getAttackerStatId());
        double D_VALUE = defender.getFinalValue(multiplier.getDefenderStatId());

        return rateCalculator.calculate(A_VALUE,D_VALUE,multiplier.getMaxLimit(), multiplier.getMinLimit());
    }
}
