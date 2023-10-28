package net.haraxx.coresystem.plugins.rpg.listener;

import net.haraxx.coresystem.plugins.rpg.item.NBTapi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        Player p = e.getWhoClicked().getKiller();
        ItemStack item = e.getCursor();
        NBTapi NBT = new NBTapi();
        if (NBT.isProtected(item)) {
            e.setCancelled(true);
        }
        // implement GUI working with custom NBT tags out of
    }
}
