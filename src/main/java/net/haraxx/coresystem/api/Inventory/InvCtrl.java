package net.haraxx.coresystem.api.Inventory;

import net.haraxx.coresystem.api.Item.ItemCtrl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class InvCtrl {
    public static Inventory inventoryPrepare(int size, String title) {
        Inventory inv = Bukkit.createInventory(null, size, title);

        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, ItemCtrl.getItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE,"§0", 103, null , null , null));
        }

        for (int i = inv.getSize()-9; i < inv.getSize(); i++) {
            inv.setItem(i, ItemCtrl.getItem(Material.GRAY_STAINED_GLASS_PANE,"§0", 103, null , null , null));
        }
        inv.setItem(inv.getSize()-5, ItemCtrl.getItem(Material.RED_STAINED_GLASS_PANE,"§cSchließen", 104, null , null , null));
        return inv;
    }

    public static Inventory inventoryPrepareEmpty(int size, String title) {
        Inventory inv = Bukkit.createInventory(null, size, title);

        for (int i = inv.getSize()-9; i < inv.getSize(); i++) {
            inv.setItem(i, ItemCtrl.getItem(Material.GRAY_STAINED_GLASS_PANE,"§0", 103, null , null , null));
        }
        inv.setItem(inv.getSize()-5, ItemCtrl.getItem(Material.RED_STAINED_GLASS_PANE,"§cSchließen", 104, null , null , null));
        return inv;
    }
}
