package net.haraxx.coresystem.commands.verification;

import net.haraxx.coresystem.api.command.PlayerCommand;
import net.haraxx.coresystem.api.command.TabCompletion;
import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.configs.WorldSpawnConfig;
import net.haraxx.coresystem.permissions.Utils;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Juyas
 * @version 20.11.2023
 * @since 20.11.2023
 */
public class UnverifyPlayer extends PlayerCommand
{

    private LuckPerms api;

    public UnverifyPlayer()
    {
        super( "unverify" );
        api = LuckPermsProvider.get();
        setSignature( "target" );
    }

    @Override
    public void onPlayerCommand( Player p, String[] args )
    {

        //TODO optimize - straight up copy

        if ( args.length == 1 )
        {
            if ( Bukkit.getPlayer( args[0] ) != null )
            {
                Player target = Bukkit.getPlayer( args[0] );
                if ( Utils.getDefaultPerms( p ) )
                {
                    User user = api.getUserManager().getUser( target.getUniqueId() );
                    user.data().remove( Node.builder( "group.default" ).build() );
                    user.data().add( Node.builder( "group.unverified" ).build() );
                    api.getUserManager().saveUser( user );

                    Location loc = WorldSpawnConfig.get().getFirstSpawnLocation();
                    target.teleport( new Location( Bukkit.getServer().getWorld( "Rom" ), loc.getBlockX() + 0.5, loc.getBlockY() + 0.125, loc.getBlockZ() + 0.5, -90, 0 ) );
                    target.sendMessage( Chat.format( "du wurdest §cGesperrt§7." ) );
                    target.sendMessage( Chat.format( "öffne im §9Discord §7ein Support ticket mit einem §6Screenshot dieser Nachricht" ) );
                    target.sendMessage( Chat.format( "ausgeführt von §6" + p.getName() ) );
                }
                else
                    p.sendMessage( Chat.format( "du hast nicht die nötigen §6Rechte §7um den §6Command §7auszuführen" ) );
            }
            else
                p.sendMessage( Chat.format( "Spieler mit dem namen §6" + args[0] + " §7konnte nicht gefunden werden" ) );
        }
        else p.sendMessage( Chat.format( "Bitte einen gültigen §6Spielernamen §7angeben" ) );

    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return TabCompletion.NONE;
    }

}
