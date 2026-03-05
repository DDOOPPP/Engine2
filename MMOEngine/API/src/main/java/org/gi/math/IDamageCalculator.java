package org.gi.math;

public interface IDamageCalculator {
    public double calculateDamage(double attackValue, double defenseValue, double D_CONSTANT);

    public double calculateCritical(double damage, double criticalMultiplier);

    public double calculateBlock(double damage, double blockReduction);
}
