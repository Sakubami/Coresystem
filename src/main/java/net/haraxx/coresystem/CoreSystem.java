package net.haraxx.coresystem;

import net.haraxx.coresystem.api.command.CommandGroup;
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

public final class CoreSystem extends JavaPlugin
{

    private static CoreSystem instance;

    private LocationConfig locationConfig;
    private RPGPlayerConfig rpgPlayerConfig;
    private WorldSpawnConfig worldSpawnConfig;

    @Override
    public void onEnable()
    {
        try
        {
            instance = this;

            //initiate Internal stuff

            //listener
            Bukkit.getPluginManager().registerEvents( new PlaceStuffIdk(), this );
            Bukkit.getPluginManager().registerEvents( new PlayerSpawn(), this );
            Bukkit.getPluginManager().registerEvents( new UnverifiedListener(), this );

            //commands

            //create command groups
            CommandGroup mainCommand = new CommandGroup( getCommand( "haraxx" ) );
            CommandGroup itemCommands = new CommandGroup( "item" );

            mainCommand.addSubCommand( itemCommands );
            mainCommand.addSubCommand( new PlayerGamemode() );
            mainCommand.addSubCommand( new Spawn() );
            mainCommand.addSubCommand( new VerifyPlayer() );
            mainCommand.addSubCommand( new UnverifyPlayer() );

            //add actual commands
            itemCommands.addSubCommand( new ItemGet() );
            itemCommands.addSubCommand( new ItemName() );
            itemCommands.addSubCommand( new ItemLore() );
            itemCommands.addSubCommand( new ItemEnchant() );

            //register main command group(s)
            mainCommand.register();

            //init core command
//            PluginCommand rawCoreCommand = Objects.requireNonNull(getCommand("haraxx"));

            //register core subcommands
//            CoreCommand coreCoreCommand = new CoreCommand();
//            coreCoreCommand.registerCoreSubCommand("item", new CMD_ITEM());
//            coreCoreCommand.registerCoreSubCommand("spawn", new CMD_SPAWN());
//            coreCoreCommand.registerCoreSubCommand("verify", new CMD_VERIFY());
//            coreCoreCommand.registerCoreSubCommand("unverify", new CMD_UNVERIFY());
//            coreCoreCommand.registerCoreSubCommand("rpg", new CMD_RPG());
//            coreCoreCommand.registerCoreSubCommand("player", new CMD_PLAYER());

            //register final command
//            rawCoreCommand.setExecutor(coreCoreCommand);
//            rawCoreCommand.setTabCompleter(coreCoreCommand);

            PluginCommand spawnCommand = Objects.requireNonNull( getCommand( "spawn" ) );
            spawnCommand.setExecutor( new SpawnCommand() );

            //init configs
            locationConfig = new LocationConfig();
            rpgPlayerConfig = new RPGPlayerConfig();
            worldSpawnConfig = new WorldSpawnConfig();

            //saving stuff
            //RPGPlayerConfig.get().autoSave();

            this.getLogger().addHandler( new LogTracker().onlyExceptions() );
        }
        catch ( Exception i )
        {
            i.printStackTrace();
        }
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
