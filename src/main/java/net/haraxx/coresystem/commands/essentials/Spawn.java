package net.haraxx.coresystem.commands.essentials;

import net.haraxx.coresystem.api.command.PlayerCommand;
import net.haraxx.coresystem.api.command.TabCompletion;
import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.configs.WorldSpawnConfig;
import net.haraxx.coresystem.permissions.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Juyas
 * @version 20.11.2023
 * @since 20.11.2023
 */
public class Spawn extends PlayerCommand
{

    public Spawn()
    {
        super( "spawn" );
        setSignature( "subcommand" );
    }

    @Override
    public void onPlayerCommand( Player p, String[] args )
    {

        //TODO optimize - straight up copy

        if ( Utils.getDefaultPerms( p ) )
        {
            if ( args.length == 1 )
            {
                switch ( args[0] )
                {
                    case "setwoldspawn" ->
                    {
                        WorldSpawnConfig.get().setWorldSpawnLocation( p.getLocation() );
                        p.sendMessage( Chat.format( "set spawnlocation to §6" + p.getLocation().getBlockX() + "§7.§6" + p.getLocation().getBlockY() + "§7.§6" + p.getLocation().getBlockZ() ) );
                    }
                    case "setfirstspawn" ->
                    {
                        WorldSpawnConfig.get().setFirstLocation( p.getLocation() );
                        p.sendMessage( Chat.format( "set firstlocation to §6" + p.getLocation().getBlockX() + "§7.§6" + p.getLocation().getBlockY() + "§7.§6" + p.getLocation().getBlockZ() ) );
                    }
                }
            }
        }
        else p.sendMessage( Chat.format( "du hast nicht die nötigen §6Rechte §7um den §6Command §7auszuführen" ) );
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return args.length == 1 ? () -> List.of( "setwoldspawn", "setfirstspawn" ) : TabCompletion.NONE;
    }
}
