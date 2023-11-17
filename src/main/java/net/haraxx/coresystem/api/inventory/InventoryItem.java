package net.haraxx.coresystem.api.inventory;

import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public interface InventoryItem
{

    Supplier<ItemStack> display();

    boolean locked();

    void callAction( CustomInventory inventory, ItemAction action );

}
