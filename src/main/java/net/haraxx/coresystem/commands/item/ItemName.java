package net.haraxx.coresystem.commands.item;

import net.haraxx.coresystem.api.command.*;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author Juyas
 * @version 19.11.2023
 * @since 19.11.2023
 */
public class ItemName extends PlayerCommand
{

    public ItemName()
    {
        super( "name", "title" );
        setSignature( "name" );
        setDescription( "Set an items name" );
        setArgumentRange( ArgumentRange.openMax( 1 ) );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if ( mainHand.getType() == Material.AIR )
        {
            player.sendMessage( "§cYou don't have any item in your main hand." );
            return;
        }
        ItemMeta meta = mainHand.getItemMeta();
        if ( meta == null )
            meta = Bukkit.getItemFactory().getItemMeta( mainHand.getType() );
        String name = String.join( " ", args );
        name = ChatColor.translateAlternateColorCodes( '&', name );
        meta.setDisplayName( name );
        mainHand.setItemMeta( meta );
        player.getInventory().setItemInMainHand( mainHand );
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        if ( !( sender instanceof Player player ) ) return TabCompletion.NONE;
        if ( args.length > 1 ) return TabCompletion.NONE;
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if ( mainHand.getType() == Material.AIR || !mainHand.hasItemMeta() ) return TabCompletion.NONE;
        return () -> List.of( mainHand.getItemMeta().getDisplayName().replace( '§', '&' ) );
    }

}
