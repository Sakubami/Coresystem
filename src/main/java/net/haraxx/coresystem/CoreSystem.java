package net.haraxx.coresystem;

import net.haraxx.coresystem.listener.PlaceStuffIdk;
import net.haraxx.coresystem.plugins.rpg.commands.ItemCommand;
import net.haraxx.coresystem.plugins.rpg.abilities.Abilities;
import net.haraxx.coresystem.plugins.rpg.abilities.abilities.BSK_tomahawk;
import net.haraxx.coresystem.plugins.zoll.LocationConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CoreSystem extends JavaPlugin {

    private static CoreSystem instance;

    public static CoreSystem getInstance() {
        return instance;
    }

    private static void setInstance(CoreSystem plugin) {
        instance = plugin;
    }

    private LocationConfig locationConfig;

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
    }

    private void init() {
        try {
            setInstance(this);

            // abilitys
            Abilities.addAbilities();

            //listener
            Bukkit.getPluginManager().registerEvents(new BSK_tomahawk(), this);
            Bukkit.getPluginManager().registerEvents(new PlaceStuffIdk(), this);

            //commands
            PluginCommand itemCommand = Objects.requireNonNull(getCommand("core"));
            itemCommand.setExecutor(new ItemCommand());
            itemCommand.setTabCompleter(new ItemCommand());

            //configs
            locationConfig = new LocationConfig();

        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    public LocationConfig getLocationConfig() { return locationConfig; }

}