package org.gi.mMOEngine.scheduler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.gi.Result;
import org.gi.mMOEngine.MMOEngine;
import org.gi.model.EntityStatData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BuffScheduler {
    BukkitRunnable runnable;
    Map<UUID, EntityStatData> entityStatDataMap = new ConcurrentHashMap<>();

    public BuffScheduler(JavaPlugin plugin){
        start();

        runnable.runTaskTimer(plugin,0L,40L);
    }

    public void start(){
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (entityStatDataMap.isEmpty()){
                    return;
                }

                for (var data : entityStatDataMap.values()){
                    data.getBuffManager().tick();
                }
            }
        };
    }

    public void stop(){
        if (runnable == null){
            return;
        }
        runnable.cancel();
    }

    public Result addPlayer(EntityStatData statData){
        if (statData == null){
            return Result.ERROR("StatData is Null");
        }
        entityStatDataMap.put(statData.getUuid(),statData);
        return Result.SUCCESS;
    }

    public Result removePlayer(UUID uuid){
        if (uuid == null){
            return Result.ERROR("UUID is Null");
        }
        if (!entityStatDataMap.containsKey(uuid)){
            return Result.ERROR("UUID: [%s] is Not Found Data".formatted(uuid));
        }
        entityStatDataMap.remove(uuid);
        return Result.SUCCESS;
    }
}
