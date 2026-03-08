package org.gi.mMOEngine.loader;

import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.gi.Result;
import org.gi.model.Setting;
import org.gi.register.ICombatRegister;

public class CombatLoader extends Loader{
    private final ICombatRegister register;
    private static double defenseConstant;
    public CombatLoader(JavaPlugin plugin, String fileName, ICombatRegister register) {
        super("CombatLoader", plugin, fileName);
        this.register = register;
    }

    @Override
    public Result load() {
        ConfigurationSection section = configuration.getConfigurationSection("");

        for (var key : section.getKeys(false)){
            if (key.equals("defenseConstant")){
                defenseConstant = section.getDouble(key);
                continue;
            }
            ConfigurationSection statSection =
                    key.equals("damage_setting") ?
                            section.getConfigurationSection(key+".type"):
                            section.getConfigurationSection(key+".stat");

            if (key.equals("damage_setting")){

                for (String type : statSection.getKeys(false)){
                    String attackerId = statSection.getString(type+".attacker");
                    String defenderId = statSection.getString(type+".defender");

                    Setting setting = new Setting(type,attackerId,defenderId,0,0);

                    var result = register.register(setting);

                    if (!result.isSuccess()){
                        return Result.ERROR("damage_setting Register Failed");
                    }
                }

                continue;
            }



            for (var stat : statSection.getKeys(false)) {
                ConfigurationSection detailSection = statSection.getConfigurationSection(stat);

                String attackStatId = detailSection.getString("attacker");
                String defenderStatId = detailSection.getString("defender");

                double minValue = detailSection.getDouble("limit.min",0);
                double maxValue = detailSection.getDouble("limit.max",0);

                Setting setting = new Setting(stat,attackStatId,defenderStatId,maxValue,minValue);

                var result = register.register(setting);

                if (!result.isSuccess()){
                    return Result.ERROR(stat+" Register Failed");
                }
            }
        }

        return Result.SUCCESS;
    }

    @Override
    public Result clear() {
        return register.clear();
    }

    public static Double getDefenseConstant(){
        return defenseConstant;
    }
}
