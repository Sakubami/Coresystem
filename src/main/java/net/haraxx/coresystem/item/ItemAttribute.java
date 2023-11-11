package net.haraxx.coresystem.item;

/**
 * @author Juyas
 * @version 10.11.2023
 * @since 10.11.2023
 */
public enum ItemAttribute
{
    HEALTH( "Leben" ),
    DEFENSE( "Rüstung" ),
    STRENGTH( "Stärke", false ),
    CRITICAL_CHANCE( "krit. Chance", false ),
    CRITICAL_DAMAGE( "krit. Schaden", false );

    private final String displayName;
    private final boolean stat;

    ItemAttribute( String displayName )
    {
        this.displayName = displayName;
        this.stat = true;
    }

    ItemAttribute( String displayName, boolean stat )
    {
        this.displayName = displayName;
        this.stat = stat;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public boolean isStat()
    {
        return stat;
    }

}
