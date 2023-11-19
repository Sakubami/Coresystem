package net.haraxx.coresystem.api.data.impl;

import net.haraxx.coresystem.api.data.model.CachedValue;
import net.haraxx.coresystem.api.data.model.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public class DatabaseCachedValue<T> implements CachedValue<T>
{

    private final String modelName;
    private final PrimaryKey modelPrimaryKey;
    private T value;
    private LocalDateTime lastUpdated;

    public DatabaseCachedValue( String modelName, PrimaryKey modelPrimaryKey )
    {
        this.modelName = modelName;
        this.modelPrimaryKey = modelPrimaryKey;
        this.value = null;
        lastUpdated = null;
    }

    @Override
    public Optional<T> get()
    {
        return Optional.ofNullable( value );
    }

    @Override
    public void update( T value )
    {
        //TODO
    }

    @Override
    public void get( Consumer<T> future )
    {
        //TODO
        //request process
    }

    @Override
    public String getAsString()
    {
        return get().map( Object::toString ).orElse( "" );
    }

    @Override
    public void updateCache()
    {
        //TODO
    }

    @Override
    public boolean isCached()
    {
        return false;
    }

    @Override
    public LocalDateTime lastUpdated()
    {
        return lastUpdated;
    }

}
