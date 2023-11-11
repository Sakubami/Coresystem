package net.haraxx.coresystem.item;

/**
 * @author Juyas
 * @version 11.11.2023
 * @since 11.11.2023
 */
public enum ItemPassiveType
{

    ;

    private final String description;

    ItemPassiveType( String description )
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
}
