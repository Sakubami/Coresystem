package net.haraxx.coresystem.item;

import org.bukkit.ChatColor;

/**
 * @author Juyas
 * @version 10.11.2023
 * @since 10.11.2023
 */
public enum ItemRarity
{

    COMMON( ChatColor.GREEN ),
    UNCOMMON( ChatColor.YELLOW ),
    RARE( ChatColor.BLUE ),
    EPIC( ChatColor.DARK_PURPLE ),
    LEGENDARY( ChatColor.GOLD ),
    MYTHIC( ChatColor.LIGHT_PURPLE ),
    DIVINE( ChatColor.AQUA );

    private final ChatColor color;

    ItemRarity( ChatColor color )
    {
        this.color = color;
    }

    public ChatColor getColor()
    {
        return color;
    }

}
