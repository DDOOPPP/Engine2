package org.gi.mMOEngine.math;

import org.gi.container.IStatContainer;
import org.gi.mMOEngine.register.StatRegister;
import org.gi.manager.IModifierManager;
import org.gi.math.IStatCalculator;
import org.gi.model.Enums.Operator;
import org.gi.model.Stat;
import org.gi.model.StatModifier;
import org.gi.register.IStatRegister;

public class StatCalculator implements IStatCalculator {
    private final IStatRegister statRegister;
    private final IModifierManager modifierManager;
    private final IStatContainer container;

    public StatCalculator(IStatRegister register, IModifierManager modifierManager,IStatContainer container){
        this.statRegister = register;
        this.modifierManager = modifierManager;
        this.container = container;
    }

    @Override
    public void recalculate() {
        for (Stat stat : statRegister.getAllStat()){
            String statId = stat.getId();

            calculateByStatId(statId);
        }
    }

    @Override
    public Double calculateByStatId(String statId) {
        var list = modifierManager.findByStatId(statId);
        var stat = statRegister.findByName(statId);

        if (stat.isEmpty()){
            return 0.00;
        }

        double base = stat.get().getDefaultValue();
        double flat = 0;
        double percent = 0;
        for (StatModifier modifier : list){
            Operator operator = modifier.getOperator();

            switch (operator){
                case FLAT -> flat += modifier.getValue();
                case PERCENT -> percent += modifier.getValue();
            }
        }
        double result = (base + flat) * (1 + percent/ 100);
        result = checkRange(stat.get(),result);
        container.notifyStatValue(statId,result);
        return result;
    }

    private double checkRange(Stat stat, double result){
        if (result <= stat.getMinValue()){
            result = stat.getMinValue();
        }

        if (result >= stat.getMaxValue()){
            result = stat.getMaxValue();
        }


        return result;
    }
}
