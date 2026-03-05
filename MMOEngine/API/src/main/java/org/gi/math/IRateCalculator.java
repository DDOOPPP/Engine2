package org.gi.math;

import org.gi.model.EntityStatData;

public interface IRateCalculator {
    /**
     * Calculate the rate based on attack and defense values.
     * @param attackValue Attack value
     * @param defenseValue Defense value
     * @param maxLimit Maximum limit
     * @param minLimit Minimum limit
     * @return Calculated rate
     */
    public double calculate(Double attackValue, Double defenseValue, Double maxLimit, Double minLimit);

    /**
     * Check if the rate is successful.
     * @param rate Calculated rate
     * @return True if successful, false otherwise
     */
    public boolean success(double rate);
}
