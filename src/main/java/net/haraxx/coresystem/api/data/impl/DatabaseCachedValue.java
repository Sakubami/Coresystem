package net.haraxx.coresystem.api.data.impl;

import net.haraxx.coresystem.api.data.model.CachedValue;
import net.haraxx.coresystem.api.data.model.PrimaryKey;

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
    private volatile T value;
    private long lastUpdated;
    private boolean changedSinceLastUpdate;

    public DatabaseCachedValue( String modelName, PrimaryKey modelPrimaryKey )
    {
        this.modelName = modelName;
        this.modelPrimaryKey = modelPrimaryKey;
        this.value = null;
        lastUpdated = 0;
    }

    @Override
    public Optional<T> get()
    {
        return Optional.ofNullable( value );
    }

    @Override
    public void update()
    {
        if ( !this.changedSinceLastUpdate ) return;
        //TODO
    }

    @Override
    public void set( T value )
    {
        synchronized(this.modelName)
        {
            this.value = value;
            this.lastUpdated = System.currentTimeMillis();
            this.changedSinceLastUpdate = true;
        }
    }

    @Override
    public void get( Consumer<T> future )
    {
        //TODO get with refreshed cache
        //request process
    }

    @Override
    public String getAsString()
    {
        return get().map( Object::toString ).orElse( "" );
    }

    @Override
    public void cache()
    {
        //TODO load cache
    }

    @Override
    public boolean isCached()
    {
        return value != null;
    }

    @Override
    public long lastUpdated()
    {
        return lastUpdated;
    }

}
