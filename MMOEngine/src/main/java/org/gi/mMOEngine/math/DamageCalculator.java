package org.gi.mMOEngine.math;

import org.gi.math.IDamageCalculator;

public class DamageCalculator implements IDamageCalculator {
    @Override
    public double calculateDamage(double attackValue, double defenseValue, double D_CONSTANT) {
        return attackValue * (D_CONSTANT / (D_CONSTANT + defenseValue));
    }

    @Override
    public double calculateCritical(double damage, double criticalMultiplier) {
        return damage * criticalMultiplier / 100;
    }

    @Override
    public double calculateBlock(double damage, double blockReduction) {
        return damage * (1 - blockReduction / 100);
    }
}
