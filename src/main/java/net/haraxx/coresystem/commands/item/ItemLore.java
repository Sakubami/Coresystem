package net.haraxx.coresystem.commands.item;

import net.haraxx.coresystem.api.command.*;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author Juyas
 * @version 19.11.2023
 * @since 19.11.2023
 */
public class ItemLore extends PlayerCommand
{

    public ItemLore()
    {
        super( "lore", "desc" );
        setMinArgs( 2 );
        setSignature( "index", "text" );
        setDescription( "Set an items lore" );
        setArgumentRange( ArgumentRange.openMax( 2 ) );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        if ( args.length < 2 )
        {
            player.sendMessage( "§cNot enough arguments." );
            return;
        }
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if ( mainHand.getType() == Material.AIR )
        {
            player.sendMessage( "§cYou don't have any item in your main hand!" );
            return;
        }
        ItemMeta meta = mainHand.getItemMeta();
        if ( meta == null ) meta = Bukkit.getItemFactory().getItemMeta( mainHand.getType() );
        int index = Integer.parseInt( args[0] );
        List<String> lore = new ArrayList<>( meta != null && meta.getLore() != null ? meta.getLore() : Collections.emptyList() );
        String[] text = new String[args.length - 1];
        System.arraycopy( args, 1, text, 0, text.length );
        String line = String.join( " ", text );
        line = ChatColor.translateAlternateColorCodes( '&', line );
        //adjust lore length if necessary
        while ( lore.size() <= index )
        {
            lore.add( "" );
        }
        lore.set( index, line );
        meta.setLore( lore );
        mainHand.setItemMeta( meta );
        player.getInventory().setItemInMainHand( mainHand );
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        if ( !( sender instanceof Player player ) ) return TabCompletion.NONE;
        if ( args.length == 0 || args.length > 2 ) return TabCompletion.NONE;
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if ( mainHand.getType() == Material.AIR || !mainHand.hasItemMeta() || mainHand.getItemMeta().getLore() == null ) return TabCompletion.NONE;
        int line = Integer.parseInt( args[0] );
        String text = mainHand.getItemMeta().getLore().get( line ).replace( '§', '&' );
        return () -> List.of( text );
    }
}
