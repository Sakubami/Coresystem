package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.DatabaseColumn;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
public record ColumnSettings(String columnName, String sqlType, boolean nonNull, boolean unique)
{

    public static final boolean DEFAULT_NON_NULL = true;
    public static final boolean DEFAULT_UNIQUE = false;

    static ColumnSettings of( DatabaseColumn columnAnnotation )
    {
        return new ColumnSettings( columnAnnotation.name(), columnAnnotation.sqlType(), columnAnnotation.nonNull(), columnAnnotation.unique() );
    }

}
