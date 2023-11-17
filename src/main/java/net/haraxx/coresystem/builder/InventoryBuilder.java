package net.haraxx.coresystem.builder;

import net.haraxx.coresystem.builder.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class InventoryBuilder {

    private final Inventory inventory;
    private final int rows;
    private final ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayname("§3").build();
    private int math(int rows) {
        rows *= 9;
        if (rows> 54) rows = 54;
        return rows;
    }

    public InventoryBuilder(int rows, String displayname) {
        this.rows = rows;
        this.inventory = Bukkit.createInventory(null, math(rows), displayname);
    }

    public InventoryBuilder buildBottomRow() {
        for (int i = inventory.getSize()-9; i < inventory.getSize(); i++) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, "§0").build());
        }
        inventory.setItem(inventory.getSize()-5, new ItemBuilder(Material.BARRIER, "§cSchließen").build());
        return this;
    }

    public InventoryBuilder fillEmpty(ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++)
            if ( itemStack != null ) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, itemStack);
                }
            } else { fillEmpty( filler ); }
        return this;
    }

    public InventoryBuilder setItem(ItemStack item, int slot) {
        if ( item != null )
            this.inventory.setItem(slot, item);
        return this;
    }

    public InventoryBuilder addItem(ItemStack item) {
        if ( item != null )
            this.inventory.addItem(item);
        return this;
    }

    public InventoryBuilder addItems(ArrayList<ItemStack> items) {
        if ( !items.isEmpty() ) {
            for (ItemStack item : items) {
                this.inventory.addItem(item);
            }
        }
        return this;
    }

    public InventoryBuilder setRow(int row, ItemStack item, boolean overwrite) {
        if ( item != null ) {
            for (int i = (row * 9)- 9; i < (row * 9); i++) {
                if (overwrite) {
                    this.inventory.setItem(i, item);
                } else if (this.inventory.getItem(i) == null) {
                    this.inventory.setItem(i, item);
                    System.out.println("HEY IM A DEBUG ------------ " + i);
                }
            }
        }   
        return this;
    }

    public InventoryBuilder fillRow( int row ) {
        setRow( row , filler, false );
        return this;
    }

    public InventoryBuilder setColumn(int column, ItemStack item, boolean overwrite) {
        if ( item != null ) {
            for (int i = column -1; i < (rows * 9); i+=9) {
                if (overwrite) {
                    this.inventory.setItem(i, item);
                } else if (this.inventory.getItem(i) == null) {
                    this.inventory.setItem(i, item);
                }
            }
        }
        return this;
    }

    public InventoryBuilder fillColumn( int column ) {
        setColumn( column , filler, false );
        return this;
    }

    public Inventory build() {
        return inventory;
    }

}
