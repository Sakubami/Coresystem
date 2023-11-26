package net.haraxx.coresystem.listener;

import net.haraxx.coresystem.builder.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlaceStuffIdk implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Location loc = event.getBlockPlaced().getLocation();
        World world = event.getBlockPlaced().getWorld();
        Player p = event.getPlayer();
        int y = loc.getBlockY();
        if (!p.isOp()) {
            if (p.getInventory().getItemInMainHand().getType().equals(Material.RED_CONCRETE_POWDER)) {
                if (event.getBlockReplacedState().getBlockData().getMaterial().equals(Material.WATER)) {
                    p.sendMessage(Chat.format("Du kannst das nicht im Wasser Platzieren!"));
                    event.setCancelled(true);
                    return;
                }
                for (int i = y; i > -75; i--) {
                    loc.setY(i-1);
                    if (!world.getBlockAt(loc).getType().isAir()) {
                        if (world.getBlockAt(loc).isLiquid()) {
                            p.sendMessage(Chat.format("Du kannst nicht über Wasser bauen!"));
                            event.setCancelled(true);
                        }
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTridentUse( PlayerInteractEvent e ) {
        if ( e.getAction().equals( Action.RIGHT_CLICK_AIR ) || e.getAction().equals( Action.RIGHT_CLICK_BLOCK ) )
        {
            if ( e.getPlayer().getItemInHand().getType().equals( Material.TRIDENT ) )
            {
                e.setCancelled( true );
            }
        }
    }
}
