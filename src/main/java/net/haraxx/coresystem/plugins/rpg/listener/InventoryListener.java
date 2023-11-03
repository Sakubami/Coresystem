package net.haraxx.coresystem.plugins.rpg.listener;

import net.haraxx.coresystem.builder.item.NBTapi;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void protectInventoryItems(InventoryClickEvent e) {
        Player p = e.getWhoClicked().getKiller();
        ItemStack item = e.getCursor();
        NBTapi NBT = new NBTapi();
        if (item != null) {
            if (NBT.getNBTTag(item, "protected") != null) {
                e.setCancelled(true);
            }
        }
        // implement GUI working with custom NBT tags out of
    }
}
