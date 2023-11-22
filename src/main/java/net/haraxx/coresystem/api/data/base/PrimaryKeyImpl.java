package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.PrimaryKey;
import net.haraxx.coresystem.api.data.model.Value;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 19.11.2023
 */
final class PrimaryKeyImpl implements PrimaryKey
{

    private final ColumnSettings settings;
    private final Value<Long> value;

    PrimaryKeyImpl( ColumnSettings settings )
    {
        this.settings = settings;
        this.value = new DatabaseValue<>(Long.class);
    }

    @Override
    public Class<Long> javaType()
    {
        return Long.TYPE;
    }

    @Override
    public ColumnSettings settings()
    {
        return settings;
    }

    @Override
    public Value<Long> value()
    {
        return value;
    }
}
