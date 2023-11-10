package net.haraxx.coresystem.listener;

import net.haraxx.coresystem.permissions.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class UnverifiedListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(Utils.isNew(p)) {
            p.sendTitle("§4Unverifiziert", "§7Lies die §6Schilder!", 2,30 ,2);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p)
            if(Utils.isNew(p))
                e.setCancelled(true);
    }
}
