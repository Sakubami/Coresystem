package net.haraxx.coresystem.api.inventory;

import java.util.HashMap;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public class FixedInventoryMask implements InventoryMask
{

    private final char[] slotMask;
    private final HashMap<Character, InventoryItem> itemMap;
    private final int size;

    public FixedInventoryMask( HashMap<Character, InventoryItem> itemMap, char... slotMask )
    {
        this.slotMask = slotMask;
        int tmp = slotMask.length % 9 == 0 ? 0 : 1;
        this.size = Math.max( Math.min( tmp + ( slotMask.length / 9 ), 54 ), 9 );
        this.itemMap = itemMap;
    }

    public FixedInventoryMask( char... slotMask )
    {
        this( new HashMap<>(), slotMask );
    }

    public FixedInventoryMask setItem( char code, InventoryItem item )
    {
        this.itemMap.put( code, item );
        return this;
    }

    @Override
    public int size()
    {
        return size;
    }

    @Override
    public void apply( InventoryItem[] items )
    {
        for ( int i = 0; i < slotMask.length; i++ )
        {
            items[i] = itemMap.get( slotMask[i] );
        }
    }

}
