package net.haraxx.coresystem.guis;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIcontroller {

    public boolean safeCompare(ItemStack item, String name) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            return meta.getDisplayName().equalsIgnoreCase(name);
        }
        return false;
    }
}
