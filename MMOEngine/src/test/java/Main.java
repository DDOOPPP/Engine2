import org.gi.mMOEngine.container.StatContainer;
import org.gi.mMOEngine.manager.ModifierManager;
import org.gi.mMOEngine.math.StatCalculator;
import org.gi.mMOEngine.register.StatRegister;
import org.gi.model.Enums;
import org.gi.model.EntityStatData;
import org.gi.model.Stat;
import org.gi.model.StatModifier;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Main {
    static Logger logger = Logger.getLogger("Test");

    public static void main(String[] args) {
        StatRegister register = new StatRegister();
        register.register(testStat());

        ModifierManager manager = new ModifierManager();
        StatContainer container = new StatContainer();
        StatCalculator calculator = new StatCalculator(register,manager,container);
        UUID uuid = UUID.randomUUID();
        logger.info(uuid.toString());
        EntityStatData data = new EntityStatData(uuid,manager,container,calculator);

        data.getManager().add(getDummy("Test1",Enums.Operator.FLAT));
        data.getManager().add(getDummy("Test2",Enums.Operator.PERCENT));

        for (var modifier : getEquipment()){
            data.getManager().add(modifier);
        }

        data.getCalculator().recalculate();

        logger.info(String.valueOf(data.getContainer().getFinalValue("testStat")));

        data.getManager().removeBySource("testEquipment");

        data.getCalculator().calculateByStatId("testStat");

        logger.info(String.valueOf(data.getContainer().getFinalValue("testStat")));
    }

    public static Stat testStat(){
        return new Stat("testStat",
                "testStat",
                Enums.StatCategory.OFFENSE,
                10,
                0,
                9999);
    }

    public static StatModifier getDummy(String modifierId,Enums.Operator operator){
        return new StatModifier(
                modifierId,
                "test",
                "testStat",
                operator,
                Enums.ModifierSource.BASE,
                20
        );
    }

    public static List<StatModifier> getEquipment(){
        List<StatModifier> list = List.of(
                new StatModifier(
                        "equipment01",
                        "testEquipment",
                        "testStat",
                        Enums.Operator.FLAT,
                        Enums.ModifierSource.EQUIPMENT,
                        50
                ),
                new StatModifier(
                        "equipment02",
                        "testEquipment",
                        "testStat",
                        Enums.Operator.PERCENT,
                        Enums.ModifierSource.EQUIPMENT,
                        10
                )
        );
        return  list;
    }
}
