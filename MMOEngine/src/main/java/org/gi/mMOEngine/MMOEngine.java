package org.gi.mMOEngine;

import org.bukkit.plugin.java.JavaPlugin;
import org.gi.mMOEngine.loader.CombatLoader;
import org.gi.mMOEngine.loader.StatLoader;
import org.gi.mMOEngine.register.CombatRegister;
import org.gi.mMOEngine.register.StatRegister;

public final class MMOEngine extends JavaPlugin {
    private static MMOEngine instance;
    private static StatLoader statLoader;
    private static CombatLoader combatLoader;
    private static StatRegister statRegister;
    private static CombatRegister combatRegister;

    @Override
    public void onEnable() {
        instance = this;
        statRegister = new StatRegister();
        statLoader = new StatLoader(this,"stat.yml",statRegister);

        var result = statLoader.load();
        if (!result.isSuccess()){
            getLogger().severe(result.getMessage());
            onDisable();
        }

        combatRegister = new CombatRegister();

        combatLoader = new CombatLoader(this,"combat.yml",combatRegister);

        result = combatLoader.load();

        if (!result.isSuccess()){
            getLogger().severe(result.getMessage());
            onDisable();
        }
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static MMOEngine getInstance(){
        return instance;
    }
}
