package net.haraxx.coresystem.api.inventory;

import org.bukkit.inventory.ItemStack;

import java.util.function.*;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public class FixedInventoryItem implements InventoryItem
{

    private final ItemStack itemStack;
    private boolean locked;

    private BiConsumer<CustomInventory, ItemAction> onCallAction;

    public FixedInventoryItem( ItemStack itemStack, boolean locked )
    {
        this.itemStack = itemStack;
        this.locked = locked;
        this.onCallAction = ( inv, action ) -> {};
    }

    public FixedInventoryItem lock( boolean locked )
    {
        this.locked = locked;
        return this;
    }

    public FixedInventoryItem onCallAction( BiConsumer<CustomInventory, ItemAction> onCallAction )
    {
        this.onCallAction = this.onCallAction.andThen( onCallAction );
        return this;
    }

    public FixedInventoryItem onCallAction( ItemAction itemAction, Consumer<CustomInventory> consumer )
    {
        return onCallAction( ( inv, action ) -> {
            if ( action == itemAction )
                consumer.accept( inv );
        } );
    }

    @Override
    public Supplier<ItemStack> display()
    {
        return () -> itemStack;
    }

    @Override
    public boolean locked()
    {
        return locked;
    }

    @Override
    public void callAction( CustomInventory inv, ItemAction action )
    {
        onCallAction.accept( inv, action );
    }

}
