package net.haraxx.coresystem;

import net.haraxx.coresystem.commands.Commands;
import net.haraxx.coresystem.listener.PlaceStuffIdk;
import net.haraxx.coresystem.commands.subcommands.ItemCommand;
import net.haraxx.coresystem.plugins.rpg.abilities.Abilities;
import net.haraxx.coresystem.plugins.rpg.abilities.abilities.BSK_TOMAHAWK;
import net.haraxx.coresystem.plugins.zoll.LocationConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CoreSystem extends JavaPlugin {

    private static CoreSystem instance;
    private LocationConfig locationConfig;

    @Override
    public void onEnable() {
        try {
            instance = this;

            // initiate Internal stuff
            Abilities.initiateAbilitys();

            //listener
            Bukkit.getPluginManager().registerEvents(new BSK_TOMAHAWK(), this);
            Bukkit.getPluginManager().registerEvents(new PlaceStuffIdk(), this);

            //commands
            PluginCommand rawCoreCommand = Objects.requireNonNull(getCommand("core"));
            Commands coreCommands = new Commands();
            coreCommands.registerCoreSubCommand("item", new ItemCommand());
            rawCoreCommand.setExecutor(coreCommands);
            rawCoreCommand.setTabCompleter(coreCommands);

            //configs
            locationConfig = new LocationConfig();

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

    public LocationConfig getLocationConfig() { return locationConfig; }
}