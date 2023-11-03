package net.haraxx.coresystem;

import net.haraxx.coresystem.commands.Commands;
import net.haraxx.coresystem.listener.PlaceStuffIdk;
import net.haraxx.coresystem.commands.subcommands.ItemCommand;
import net.haraxx.coresystem.plugins.rpg.player.RPGPlayerConfig;
import net.haraxx.coresystem.plugins.zoll.LocationConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CoreSystem extends JavaPlugin {

    private static CoreSystem instance;
    private LocationConfig locationConfig;
    private RPGPlayerConfig rpgPlayerConfig;

    @Override
    public void onEnable() {
        try {
            instance = this;

            //initiate Internal stuff
            //init player

            //listener
            Bukkit.getPluginManager().registerEvents(new PlaceStuffIdk(), this);

            //commands
            //init core command
            PluginCommand rawCoreCommand = Objects.requireNonNull(getCommand("core"));

            //register core subcommands
            Commands coreCommands = new Commands();
            coreCommands.registerCoreSubCommand("item", new ItemCommand());

            //register final command
            rawCoreCommand.setExecutor(coreCommands);
            rawCoreCommand.setTabCompleter(coreCommands);

            //configs
            locationConfig = new LocationConfig();
            rpgPlayerConfig = new RPGPlayerConfig();

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
}