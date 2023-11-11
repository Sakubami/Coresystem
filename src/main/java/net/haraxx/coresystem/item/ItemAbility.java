package net.haraxx.coresystem.item;

/**
 * @author Juyas
 * @version 10.11.2023
 * @since 10.11.2023
 */
public enum ItemAbility
{

    NONE("Keine");

    private final String displayName;

    ItemAbility( String displayName )
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
