package net.haraxx.coresystem.listener;

import net.haraxx.coresystem.builder.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        Player p = e.getWhoClicked().getKiller();

    }
}
