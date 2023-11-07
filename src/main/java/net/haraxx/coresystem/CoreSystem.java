package net.haraxx.coresystem;

import net.haraxx.coresystem.commands.Commands;
import net.haraxx.coresystem.commands.subcommands.Spawn;
import net.haraxx.coresystem.commands.subcommands.Unverify;
import net.haraxx.coresystem.commands.subcommands.Verify;
import net.haraxx.coresystem.listener.PlaceStuffIdk;
import net.haraxx.coresystem.commands.subcommands.Item;
import net.haraxx.coresystem.plugins.rpg.player.RPGPlayerConfig;
import net.haraxx.coresystem.plugins.spawning.NewPlayerConfig;
import net.haraxx.coresystem.plugins.spawning.PlayerSpawn;
import net.haraxx.coresystem.plugins.spawning.WorldSpawnConfig;
import net.haraxx.coresystem.plugins.zoll.LocationConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CoreSystem extends JavaPlugin {

    private static CoreSystem instance;

    private LocationConfig locationConfig;
    private RPGPlayerConfig rpgPlayerConfig;
    private WorldSpawnConfig worldSpawnConfig;
    private NewPlayerConfig newPlayerConfig;

    @Override
    public void onEnable() {
        try {
            instance = this;

            //initiate Internal stuff
            //init player

            //listener
            Bukkit.getPluginManager().registerEvents(new PlaceStuffIdk(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerSpawn(), this);

            //commands
            //init core command
            PluginCommand rawCoreCommand = Objects.requireNonNull(getCommand("haraxx"));

            //register core subcommands
            Commands coreCommands = new Commands();
            coreCommands.registerCoreSubCommand("item", new Item());
            coreCommands.registerCoreSubCommand("spawn", new Spawn());
            coreCommands.registerCoreSubCommand("verify", new Verify());
            coreCommands.registerCoreSubCommand("unverify", new Unverify());

            //register final command
            rawCoreCommand.setExecutor(coreCommands);
            rawCoreCommand.setTabCompleter(coreCommands);

            //init configs
            locationConfig = new LocationConfig();
            rpgPlayerConfig = new RPGPlayerConfig();
            worldSpawnConfig = new WorldSpawnConfig();
            newPlayerConfig = new NewPlayerConfig();

        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        System.out.println("HaraxxCore » disabling [ Core ]");
    }

    public static CoreSystem getInstance() {
        return instance;
    }

    public LocationConfig getLocationConfig() { return locationConfig; }
    public RPGPlayerConfig getRPGPlayerConfig() { return rpgPlayerConfig; }
    public WorldSpawnConfig getWorldSpawnLocation() { return worldSpawnConfig; }
    public NewPlayerConfig getNewPlayerConfig() { return newPlayerConfig; }
}