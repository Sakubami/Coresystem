package net.haraxx.coresystem.api.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A command class derived from {@link BukkitCommand} to support only players executing it.
 * If a non-player tries to call this command, they face the {@link #MESSAGE_PLAYERS_ONLY} as message in chat
 *
 * @author Juyas
 * @version 24.11.2023
 * @see BukkitCommand
 * @see CommandGroup
 * @since 12.11.2023
 */
public abstract class PlayerCommand extends BukkitCommand
{

    private static final String MESSAGE_PLAYERS_ONLY = "§cDieser Befehl ist nur für Spieler.";

    public PlayerCommand( String name, String... aliases )
    {
        super( name, aliases );
    }

    @Override
    public final void onCommand( CommandSender sender, String[] args )
    {
        if ( sender instanceof Player player )
        {
            onPlayerCommand( player, args );
        }
        else sender.sendMessage( MESSAGE_PLAYERS_ONLY );
    }

    public abstract void onPlayerCommand( Player player, String[] args );

}
