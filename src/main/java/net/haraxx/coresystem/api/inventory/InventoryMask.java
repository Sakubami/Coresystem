package net.haraxx.coresystem.api.inventory;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public interface InventoryMask
{

    int size();

    void apply( InventoryItem[] items );

}
