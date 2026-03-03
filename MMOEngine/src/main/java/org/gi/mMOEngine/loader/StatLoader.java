package org.gi.mMOEngine.loader;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.gi.Result;
import org.gi.model.Enums.StatCategory;
import org.gi.model.Stat;
import org.gi.register.IStatRegister;
import java.util.logging.Logger;

public class StatLoader extends Loader{
    private final IStatRegister register;

    public StatLoader (JavaPlugin plugin, String fileName,IStatRegister register) {
        super("StatLoader", plugin, fileName);
        this.register = register;
    }

    @Override
    public Result load() {
        try{
            ConfigurationSection section = configuration.getConfigurationSection("stat");
            if (section == null){
                return Result.ERROR("Section is Null");
            }
            for (var key : section.getKeys(false)){
                ConfigurationSection statSection = section.getConfigurationSection(key);
                if (statSection == null){
                    logger.severe(key+ "is Null");
                    continue;
                }

                String display = statSection.getString("display");
                StatCategory category = StatCategory.valueOf(statSection.getString("category"));
                double defaultValue = statSection.getDouble("defaultValue");
                double minvalue = statSection.getDouble("minValue");
                double maxValue = statSection.getDouble("maxValue");

                Stat stat = new Stat(key,display,category,defaultValue,minvalue,maxValue);

                var result = register.register(stat);
                if (!result.isSuccess()){
                    logger.severe(result.getMessage());
                    continue;
                }
                logger.info(key+" "+result.getMessage());
            }

        }catch (Exception e){
            return Result.EXCEPTION(e);
        }
        return Result.SUCCESS;
    }

    @Override
    public Result clear() {
        return register.clear();
    }
}
