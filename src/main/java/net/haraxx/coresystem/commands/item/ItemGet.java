package net.haraxx.coresystem.commands.item;

import net.haraxx.coresystem.api.command.PlayerCommand;
import net.haraxx.coresystem.api.command.TabCompletion;
import net.haraxx.coresystem.api.util.Try;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Juyas
 * @version 12.11.2023
 * @since 12.11.2023
 */
public class ItemGet extends PlayerCommand
{

    public ItemGet()
    {
        super( "get", "give", "create", "new" );
        setDescription( "Erhalte ein neuen ItemStack mit dem gegebenen Material und Menge" );
        setSignature( "material", "menge" );
        setMinArgs( 1 );
    }

    @Override
    public void onPlayerCommand( Player player, String[] args )
    {
        Material material = Try.silent( () -> Material.matchMaterial( args[0] ) );
        int amount = Try.silent( () -> Integer.parseInt( args[1] ), 1 );
        if ( material == null )
            player.sendMessage( "§cMaterial nicht erkannt: \"" + args[0] + "\"" );
        else player.getInventory().addItem( new ItemStack( material, amount ) );
    }

    @Override
    public TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return args.length == 1 ? TabCompletion.MATERIAL.concat( TabCompletion.MATERIAL_KEYS ) : TabCompletion.NONE;
    }

}
