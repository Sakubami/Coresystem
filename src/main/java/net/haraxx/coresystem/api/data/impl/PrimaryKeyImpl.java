package net.haraxx.coresystem.api.data.impl;

import net.haraxx.coresystem.api.data.model.PrimaryKey;
import net.haraxx.coresystem.api.data.model.Value;

/**
 * @author Juyas
 * @version 19.11.2023
 * @since 19.11.2023
 */
public class PrimaryKeyImpl implements PrimaryKey
{

    private final ColumnSettings settings;
    private final Value<Long> value;

    public PrimaryKeyImpl( ColumnSettings settings )
    {
        this.settings = settings;
        this.value = new DatabaseValue<>();
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
