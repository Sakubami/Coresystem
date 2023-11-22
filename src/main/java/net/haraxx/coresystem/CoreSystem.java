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
        try
        {
            //listener
            Bukkit.getPluginManager().registerEvents( new PlaceStuffIdk(), this );
            Bukkit.getPluginManager().registerEvents( new PlayerSpawn(), this );
            Bukkit.getPluginManager().registerEvents( new UnverifiedListener(), this );

            //commands
            registerCommands();

            //init configs
            locationConfig = new LocationConfig();
            rpgPlayerConfig = new RPGPlayerConfig();
            worldSpawnConfig = new WorldSpawnConfig();
        }
        catch ( Exception e )
        {
            getLogger().log( Level.WARNING, "error while enabling CoreSystem", e );
        }
    }

    private void registerCommands()
    {
        //TODO replace with new command system
        PluginCommand spawnCommand = Objects.requireNonNull( getCommand( "spawn" ) );
        spawnCommand.setExecutor( new SpawnCommand() );

        //create command groups
        CommandGroup mainCommand = new CommandGroup( getCommand( "haraxx" ) );
        CommandGroup itemCommands = new CommandGroup( "item" );

        //haraxx item
        mainCommand.addSubCommand( itemCommands );
        itemCommands.addSubCommand( new ItemGet() );
        itemCommands.addSubCommand( new ItemName() );
        itemCommands.addSubCommand( new ItemLore() );
        itemCommands.addSubCommand( new ItemEnchant() );
        //haraxx gm
        mainCommand.addSubCommand( new PlayerGamemode() );
        //haraxx spawn
        mainCommand.addSubCommand( new Spawn() );
        //haraxx verify
        mainCommand.addSubCommand( new VerifyPlayer() );
        //haraxx unverify
        mainCommand.addSubCommand( new UnverifyPlayer() );

        //register main command group(s)
        Try.logRaw( mainCommand::register );
    }

    @Override
    public void onDisable()
    {
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
