package net.haraxx.coresystem.api.data.impl;

import net.haraxx.coresystem.api.data.model.Value;

import java.util.Optional;

/**
 * @author Juyas
 * @version 19.11.2023
 * @since 19.11.2023
 */
public final class DatabaseValue<T> implements Value<T>
{

    private T value;

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
