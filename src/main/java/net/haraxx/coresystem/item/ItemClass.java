package net.haraxx.coresystem.item;

/**
 * @author Juyas
 * @version 10.11.2023
 * @since 10.11.2023
 */
public enum ItemClass
{

    DEFAULT("Alle Klassen"),
    BERSERK("Nur Beserker"),
    MAGE("Nur Magier");

    private final String displayName;

    ItemClass( String displayName )
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }
}
