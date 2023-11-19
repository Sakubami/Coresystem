package net.haraxx.coresystem;

import net.haraxx.coresystem.configs.WorldSpawnConfig;
import net.haraxx.coresystem.listener.PlayerSpawn;
import net.haraxx.coresystem.listener.UnverifiedListener;
import net.haraxx.coresystem.plugins.rpg.player.RPGPlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoreSystem extends JavaPlugin {

    private static CoreSystem instance;

    private RPGPlayerConfig rpgPlayerConfig;
    private WorldSpawnConfig worldSpawnConfig;

    @Override
    public void onEnable() {
        try {
            instance = this;

            //initiate Internal stuff

            //listener
            Bukkit.getPluginManager().registerEvents(new PlayerSpawn(), this);
            Bukkit.getPluginManager().registerEvents(new UnverifiedListener(), this);

            //init configs
            worldSpawnConfig = new WorldSpawnConfig();
            this.getLogger().addHandler( new LogTracker().onlyExceptions() );
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }

    public static CoreSystem getInstance() {
        return instance;
    }

    public WorldSpawnConfig getWorldSpawnLocation() { return worldSpawnConfig; }
}