package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.Value;

import java.util.Optional;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 19.11.2023
 */
final class DatabaseValue<T> implements Value<T>
{

    private T value;
    private final Class<T> type;

    DatabaseValue( Class<T> type )
    {
        this.type = type;
    }

    @Override
    public Class<T> type()
    {
        return type;
    }

    @Override
    public Optional<T> get()
    {
        return Optional.ofNullable( value );
    }

    @Override
    public String getAsString()
    {
        return get().map( Object::toString ).orElse( "" );
    }

    @Override
    public void set( T value )
    {
        this.value = value;
    }

}
