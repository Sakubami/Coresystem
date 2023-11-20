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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Juyas
 * @version 20.11.2023
 * @since 20.11.2023
 */
public class VerifyPlayer extends PlayerCommand
{

    private LuckPerms api;

    public VerifyPlayer()
    {
        super( "verify" );
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
                if ( Utils.isSupporter( p ) )
                {
                    User user = api.getUserManager().getUser( target.getUniqueId() );
                    user.data().remove( Node.builder( "group.unverified" ).build() );
                    user.data().add( Node.builder( "group.default" ).build() );
                    user.data().add( Node.builder( "group.i" ).build() );
                    api.getUserManager().saveUser( user );

                    target.teleport( WorldSpawnConfig.get().getWorldSpawnLocation() );
                    target.sendMessage( Chat.format( "du wurdest §aFreigeschaltet§7. §6Viel Spass auf unserem Server!" ) );
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
