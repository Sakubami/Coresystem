package net.haraxx.coresystem.api.data.impl;

import net.haraxx.coresystem.api.data.model.*;

/**
 * @author Juyas
 * @version 19.11.2023
 * @since 19.11.2023
 */
public record ColumnImpl<T>(ColumnSettings settings, CachedValue<T> value, Class<T> javaType) implements Column<T>
{

    public static <T> ColumnImpl<T> of( Class<T> javaType, ColumnSettings settings, String model, PrimaryKey primaryKey )
    {
        return new ColumnImpl<>( settings, new DatabaseCachedValue<>( model, primaryKey ), javaType );
    }

}
