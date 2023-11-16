package net.haraxx.coresystem;

import net.haraxx.coresystem.commands.CoreCommand;
import net.haraxx.coresystem.commands.SpawnCommand;
import net.haraxx.coresystem.commands.subcommands.*;
import net.haraxx.coresystem.listener.PlaceStuffIdk;
import net.haraxx.coresystem.listener.UnverifiedListener;
import net.haraxx.coresystem.plugins.rpg.player.RPGPlayerConfig;
import net.haraxx.coresystem.listener.PlayerSpawn;
import net.haraxx.coresystem.configs.WorldSpawnConfig;
import net.haraxx.coresystem.plugins.zoll.LocationConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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