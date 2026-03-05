package org.gi.mMOEngine.math;

import org.gi.math.IRateCalculator;

public class RateCalculator implements IRateCalculator {
    @Override
    public double calculate(Double attackValue, Double defenseValue, Double maxLimit, Double minLimit) {
        double result = defenseValue / (defenseValue + attackValue) * maxLimit;

        if (result <= minLimit){
            return minLimit;
        }

        if (result >= maxLimit){
            return maxLimit;
        }

        return result;
    }

    @Override
    public boolean success(double rate) {
        double random = Math.random() * 100;
        if (random <= rate)
            return true;
        return false;
    }
}
