package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.*;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 19.11.2023
 */
final class DataColumn<T> implements Column<T>
{

    private final ColumnSettings settings;
    private final CachedValue<T> cachedValue;
    private final Class<T> javaType;

    private DataColumn( Class<T> javaType, ColumnSettings settings, Model model, PrimaryKey primaryKey )
    {
        this.javaType = javaType;
        this.settings = settings;
        this.cachedValue = new DatabaseCachedValue<>( javaType, settings.columnName(), model, primaryKey );
    }

    @Override
    public Class<T> javaType()
    {
        return javaType;
    }

    @Override
    public CachedValue<T> value()
    {
        return cachedValue;
    }

    @Override
    public ColumnSettings settings()
    {
        return settings;
    }

    static <T> DataColumn<T> of( Class<T> javaType, ColumnSettings settings, Model model, PrimaryKey primaryKey )
    {
        return new DataColumn<>( javaType, settings, model, primaryKey );
    }

}
