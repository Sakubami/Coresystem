package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.DatabaseColumn;

/**
 * Provides meta data for a property in a model.
 *
 * @author Juyas
 * @version 28.11.2023
 * @see DatabaseColumn
 * @since 16.11.2023
 */
public record ColumnSettings(String columnName, String sqlType, boolean nonNull, boolean unique, boolean unicodePrefixed)
{

    public static final boolean DEFAULT_NON_NULL = true;
    public static final boolean DEFAULT_UNIQUE = false;
    public static final boolean DEFAULT_UNICODE_PREFIXED = false;

    static ColumnSettings of( DatabaseColumn columnAnnotation )
    {
        return new ColumnSettings( columnAnnotation.name(), columnAnnotation.sqlType(), columnAnnotation.nonNull(), columnAnnotation.unique(), columnAnnotation.unicode() );
    }

}
