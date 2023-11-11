package net.haraxx.coresystem.item;

import org.bukkit.ChatColor;

/**
 * @author Juyas
 * @version 10.11.2023
 * @since 10.11.2023
 */
public enum ItemEnchantmentType
{
    ;

    private final String displayName;
    private final ChatColor overrideColor;

    ItemEnchantmentType( String displayName )
    {
        this.displayName = displayName;
        this.overrideColor = null;
    }

    ItemEnchantmentType( String displayName, ChatColor overrideColor )
    {
        this.displayName = displayName;
        this.overrideColor = overrideColor;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public ChatColor getOverrideColor()
    {
        return overrideColor;
    }

}
