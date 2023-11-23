package net.haraxx.coresystem;

import net.haraxx.coresystem.api.command.CommandGroup;
import net.haraxx.coresystem.api.util.Try;
import net.haraxx.coresystem.commands.essentials.PlayerGamemode;
import net.haraxx.coresystem.commands.essentials.Spawn;
import net.haraxx.coresystem.commands.item.*;
import net.haraxx.coresystem.commands.old.SpawnCommand;
import net.haraxx.coresystem.commands.verification.UnverifyPlayer;
import net.haraxx.coresystem.commands.verification.VerifyPlayer;
import net.haraxx.coresystem.configs.WorldSpawnConfig;
import net.haraxx.coresystem.listener.*;
import net.haraxx.coresystem.plugins.rpg.player.RPGPlayerConfig;
import net.haraxx.coresystem.plugins.zoll.LocationConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public final class CoreSystem extends JavaPlugin
{

    private static CoreSystem instance;

    private LocationConfig locationConfig;
    private RPGPlayerConfig rpgPlayerConfig;
    private WorldSpawnConfig worldSpawnConfig;

    @Override
    public void onEnable()
    {
        instance = this;
        Try.silent( () -> this.getLogger().addHandler( new LogTracker().onlyExceptions() ) );

        //TODO load properties for database
        //DatabaseHandler.getInstance().load( properties );

        Try.log( this::registerListeners, Level.WARNING, "error in listeners in CoreSystem" );
        Try.log( this::registerCommands, Level.WARNING, "error in commands in CoreSystem" );
        Try.log( this::initConfigs, Level.WARNING, "error loading configurations in CoreSystem" );
    }

    private void initConfigs()
    {
        locationConfig = new LocationConfig();
        rpgPlayerConfig = new RPGPlayerConfig();
        worldSpawnConfig = new WorldSpawnConfig();
    }

    private void registerListeners()
    {
        Bukkit.getPluginManager().registerEvents( new PlaceStuffIdk(), this );
        Bukkit.getPluginManager().registerEvents( new PlayerSpawn(), this );
        Bukkit.getPluginManager().registerEvents( new UnverifiedListener(), this );
    }

    private void registerCommands()
    {
        //TODO replace /spawn with new command system
        PluginCommand spawnCommand = Objects.requireNonNull( getCommand( "spawn" ) );
        spawnCommand.setExecutor( new SpawnCommand() );

        //create command groups
        CommandGroup mainCommand = new CommandGroup( getCommand( "haraxx" ) );
        CommandGroup itemCommands = new CommandGroup( "item" );

        // - /haraxx item
        mainCommand.addSubCommand( itemCommands );
        itemCommands.addSubCommand( new ItemGet() );
        itemCommands.addSubCommand( new ItemName() );
        itemCommands.addSubCommand( new ItemLore() );
        itemCommands.addSubCommand( new ItemEnchant() );
        // - /haraxx gm
        mainCommand.addSubCommand( new PlayerGamemode() );
        // - /haraxx spawn
        mainCommand.addSubCommand( new Spawn() );
        // - /haraxx verify
        mainCommand.addSubCommand( new VerifyPlayer() );
        // - /haraxx unverify
        mainCommand.addSubCommand( new UnverifyPlayer() );

        //register main command group(s)
        Try.logRaw( mainCommand::register );
    }

    @Override
    public void onDisable()
    {
        //TODO dont forget to shut it down, once its getting booted up
        //DatabaseHandler.getInstance().shutdown( false );
        System.out.println( " » disabling [ Core ]" );
    }

    public static CoreSystem getInstance()
    {
        return instance;
    }

    public LocationConfig getLocationConfig()
    {
        return locationConfig;
    }

    public RPGPlayerConfig getRPGPlayerConfig()
    {
        return rpgPlayerConfig;
    }

    public WorldSpawnConfig getWorldSpawnLocation()
    {
        return worldSpawnConfig;
    }
}
