package net.haraxx.coresystem.commands.item;

import net.haraxx.coresystem.api.command.PlayerCommand;
import net.haraxx.coresystem.api.command.TabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author Juyas
 * @version 19.11.2023
 * @since 19.11.2023
 */
public class ItemEnchant extends PlayerCommand
{

    public ItemEnchant()
    {
        super( "enchant", "ench" );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        if ( args.length == 1 )
            args = new String[] { args[0], "1" };
        if ( args.length != 2 )
        {
            player.sendMessage( "§cInvalid arguments." );
            return;
        }
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if ( mainHand.getType() == Material.AIR )
        {
            player.sendMessage( "§cYou dont have any item in your hand" );
            return;
        }
        ItemMeta meta = mainHand.hasItemMeta() ? mainHand.getItemMeta() : Bukkit.getItemFactory().getItemMeta( mainHand.getType() );
        Objects.requireNonNull( meta, "Meta is null, that should not be" );
        String enchant = args[0];
        Optional<Enchantment> optionalEnchantment = Arrays.stream( Enchantment.values() )
                .filter( ench -> ench.getKey().toString().equalsIgnoreCase( enchant ) || ench.getName().equalsIgnoreCase( enchant ) )
                .findFirst();
        if ( !optionalEnchantment.isPresent() )
        {
            player.sendMessage( "§cNo match for \"" + enchant + "\"." );
            return;
        }
        Enchantment enchantment = optionalEnchantment.get();
        int level;
        try
        {
            level = Integer.parseInt( args[1] );
            if ( level < 1 || level > 255 )
            {
                player.sendMessage( "§cValue \"" + level + "\" is out of bounds [1,255]" );
                return;
            }
        }
        catch ( Exception e )
        {
            player.sendMessage( "§c" + args[1] + " does not seem to be a valid number." );
            return;
        }
        if ( meta.hasEnchant( enchantment ) )
            meta.removeEnchant( enchantment );
        else if ( meta.hasConflictingEnchant( enchantment ) )
            player.sendMessage( "§cThis item will have a conflicting enchantment." );
        meta.addEnchant( enchantment, level, true );
        mainHand.setItemMeta( meta );
        player.getInventory().setItemInMainHand( mainHand );
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return args.length == 1 ? TabCompletion.ENCHANTMENTS.concat( TabCompletion.ENCHANTMENT_KEYS ) : TabCompletion.NONE;
    }
}
