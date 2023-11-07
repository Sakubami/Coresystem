package net.haraxx.coresystem;

import net.haraxx.coresystem.commands.CoreCommand;
import net.haraxx.coresystem.commands.SpawnCommand;
import net.haraxx.coresystem.commands.subcommands.Spawn;
import net.haraxx.coresystem.commands.subcommands.Unverify;
import net.haraxx.coresystem.commands.subcommands.Verify;
import net.haraxx.coresystem.listener.PlaceStuffIdk;
import net.haraxx.coresystem.commands.subcommands.Item;
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

    private LocationConfig locationConfig;
    private RPGPlayerConfig rpgPlayerConfig;
    private WorldSpawnConfig worldSpawnConfig;

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
            CoreCommand coreCoreCommand = new CoreCommand();
            coreCoreCommand.registerCoreSubCommand("item", new Item());
            coreCoreCommand.registerCoreSubCommand("spawn", new Spawn());
            coreCoreCommand.registerCoreSubCommand("verify", new Verify());
            coreCoreCommand.registerCoreSubCommand("unverify", new Unverify());

            //register final command
            rawCoreCommand.setExecutor(coreCoreCommand);
            rawCoreCommand.setTabCompleter(coreCoreCommand);

            PluginCommand spawnCommand = Objects.requireNonNull(getCommand("spawn"));
            spawnCommand.setExecutor(new SpawnCommand());

            //init configs
            locationConfig = new LocationConfig();
            rpgPlayerConfig = new RPGPlayerConfig();
            worldSpawnConfig = new WorldSpawnConfig();

        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        System.out.println(" » disabling [ Core ]");
    }

    public static CoreSystem getInstance() {
        return instance;
    }

    public LocationConfig getLocationConfig() { return locationConfig; }
    public RPGPlayerConfig getRPGPlayerConfig() { return rpgPlayerConfig; }
    public WorldSpawnConfig getWorldSpawnLocation() { return worldSpawnConfig; }
}