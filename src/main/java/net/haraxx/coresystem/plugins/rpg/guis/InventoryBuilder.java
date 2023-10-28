package net.haraxx.coresystem.plugins.rpg.guis;

import net.haraxx.coresystem.plugins.rpg.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryBuilder {

    private final Inventory inventory;
    private final ItemStack filler = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "§0").build();
    private int math(int math) {
        if ((math * 9)> 54) math = 54;
        return math * 9;
    }

    public InventoryBuilder(int rows) {
        this.inventory = Bukkit.createInventory(null, math(rows));
    }

    public InventoryBuilder(int rows, String displayname) {
        this.inventory = Bukkit.createInventory(null, math(rows), displayname);
    }

    public InventoryBuilder buildBottomRow() {
        for (int i = inventory.getSize()-9; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "§0").build());   //TODO REPLACE ITEM PLS
        }
        inventory.setItem(inventory.getSize()-5, new ItemBuilder(Material.BARRIER, "§cSchließen").addNBTTag("protected", "true").build());  //TODO REPLACE ITEM PLS
        return this;
    }

    public InventoryBuilder fillEmpty(ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++)
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, itemStack);
            }
        return this;
    }

    public InventoryBuilder fillEmpty() {
        fillEmpty(filler);
        return this;
    }

    public InventoryBuilder setItem(ItemStack item, int slot) {
        this.inventory.setItem(slot, item);
        return this;
    }

    public InventoryBuilder addItem(ItemStack item) {
        this.inventory.addItem(item);
        return this;
    }

    public InventoryBuilder fillRow(int row) {
        return setRow(row, filler, false);
    }

    public InventoryBuilder setRow(int row, ItemStack item, boolean overwrite) {
        for (int i = (row * 9)- 9; i < (row * 9 )- 1; i++) {
            if (overwrite) {
                this.inventory.setItem(i, item);
            } else if (this.inventory.getItem(i) == null) {
                this.inventory.setItem(i, item);
            }
        }
        return this;
    }

    public InventoryBuilder fillColumn(int row) {
        return setColumn(row, filler, false);
    }

    public InventoryBuilder setColumn(int column, ItemStack item, boolean overwrite) {
        for (int i = (column * 9)- 1; i < (column * 9 )- 9; i+=9) {
            if (overwrite) {
                this.inventory.setItem(i, item);
            } else if (this.inventory.getItem(i) == null) {
                this.inventory.setItem(i, item);
            }
        }
        return this;
    }

    public InventoryBuilder addItems(ArrayList<ItemStack> items) {
        for (ItemStack item : items) {
            this.inventory.addItem(item);
        }
        return this;
    }

    public Inventory build() {
        return inventory;
    }

    /*
    public Inventory inventoryPrepare(int size, String title) {
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

    public Inventory inventoryPrepareEmpty(int size, String title) {
        Inventory inv = Bukkit.createInventory(null, size, title);

        for (int i = inv.getSize()-9; i < inv.getSize(); i++) {
            inv.setItem(i, ItemCtrl.getItem(Material.GRAY_STAINED_GLASS_PANE,"§0", 103, null , null , null));
        }
        inv.setItem(inv.getSize()-5, ItemCtrl.getItem(Material.RED_STAINED_GLASS_PANE,"§cSchließen", 104, null , null , null));
        return inv;
    }

     */

}
