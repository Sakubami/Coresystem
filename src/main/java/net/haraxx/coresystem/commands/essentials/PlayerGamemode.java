package net.haraxx.coresystem.commands.essentials;

import net.haraxx.coresystem.api.command.PlayerCommand;
import net.haraxx.coresystem.api.command.TabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Juyas
 * @version 19.11.2023
 * @since 19.11.2023
 */
public class PlayerGamemode extends PlayerCommand
{

    public PlayerGamemode()
    {
        super( "gamemode", "gm" );
        setMinArgs( 1 );
        setSignature( "mode", "target" );
        setPermission( "haraxx.essentials.gamemode" );
        setDescription( "Sets the players or targets gamemode" );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        GameMode gameMode;
        try
        {
            gameMode = GameMode.valueOf( args[0].toUpperCase() );
        }
        catch ( Exception ignored )
        {
        }
        gameMode = GameMode.getByValue( Integer.parseInt( args[0] ) );
        if ( gameMode == null )
        {
            player.sendMessage( "§cUnknown gamemode \"" + args[0] + "\"" );
            return;
        }
        Player target = args.length == 1 ? player : Bukkit.getPlayer( args[1] );
        if ( target == null )
        {
            player.sendMessage( "§cInvalid target." );
            return;
        }
        target.setGameMode( gameMode );
        player.sendMessage( "§6" + target.getPlayer() + "'s gamemode changed to " + gameMode.name() );
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        switch ( args.length )
        {
            case 1 ->
            {
                return TabCompletion.GAMEMODE.concat( TabCompletion.GAMEMODE_VALUES );
            }
            case 2 ->
            {
                return TabCompletion.PLAYERS;
            }
        }
        return TabCompletion.NONE;
    }
}
