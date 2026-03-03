package org.gi.mMOEngine.loader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.gi.Result;

import java.io.File;
import java.util.logging.Logger;

public abstract class Loader {
    public final String loaderName;
    public final File dataFolder;
    public final FileConfiguration configuration;
    public final Logger logger;

    public Loader(String loaderName, JavaPlugin plugin, String fileName){
        this.loaderName = loaderName;
        this.logger = plugin.getLogger();
        this.dataFolder = plugin.getDataFolder();

        File configFile = new File(dataFolder,fileName);
        if (!configFile.exists()){
            plugin.saveResource(fileName,false);
        }

        configuration = YamlConfiguration.loadConfiguration(configFile);
    }

    public abstract Result load();

    public abstract Result clear();

    public Result reload(){
        logger.info(loaderName + "Reload...");
        var result = clear();
        if (!result.isSuccess()){
            logger.warning(loaderName+" "+result.getMessage());
            return result;
        }

        return load();
    }
}
