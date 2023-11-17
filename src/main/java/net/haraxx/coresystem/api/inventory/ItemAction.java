package net.haraxx.coresystem.api.inventory;

import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public enum ItemAction
{

    LEFT_CLICK( ClickType.LEFT ),
    RIGHT_CLICK( ClickType.RIGHT ),
    SHIFT_LEFT_CLICK( ClickType.SHIFT_LEFT ),
    SHIFT_RIGHT_CLICK( ClickType.SHIFT_RIGHT ),
    MIDDLE_CLICK( ClickType.MIDDLE ),
    DROP( ClickType.DROP ),
    CONTROL_DROP( ClickType.CONTROL_DROP ),
    DOUBLE_CLICK( ClickType.DOUBLE_CLICK );

    private final ClickType clickTypeEquivalent;

    ItemAction( ClickType clickTypeEquivalent )
    {
        this.clickTypeEquivalent = clickTypeEquivalent;
    }

    public ClickType getClickTypeEquivalent()
    {
        return clickTypeEquivalent;
    }

    public static ItemAction getForClickType( ClickType clickType )
    {
        return Arrays.asList( values() ).stream()
                .filter( action -> action.clickTypeEquivalent == clickType )
                .findFirst()
                .orElse( null );
    }

}
