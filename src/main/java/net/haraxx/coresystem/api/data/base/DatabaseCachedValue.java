package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.*;
import net.haraxx.coresystem.api.data.query.DatabaseHandler;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
final class DatabaseCachedValue<T> implements CachedValue<T>
{

    private final Object lock = new Object();

    private final Class<T> type;
    private final String fieldName;
    private final String modelName;
    private final String modelSchema;
    private final PrimaryKey modelPrimaryKey;
    private volatile T value;
    private long lastUpdated;
    private boolean changedSinceLastUpdate;

    DatabaseCachedValue( Class<T> type, String fieldName, Model model, PrimaryKey modelPrimaryKey )
    {
        this.modelName = model.table();
        this.modelSchema = model.schema();
        this.modelPrimaryKey = modelPrimaryKey;
        this.fieldName = fieldName;
        this.type = type;
        this.value = null;
        lastUpdated = 0;
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
    public void update()
    {
        if ( !this.changedSinceLastUpdate ) return;
        DatabaseHandler.getInstance().updateField( modelPrimaryKey, modelSchema, modelName, fieldName, this );
    }

    @Override
    public void set( T value )
    {
        synchronized(this.lock)
        {
            this.value = value;
            this.changedSinceLastUpdate = true;
            updated();
        }
    }

    @Override
    public void get( Consumer<T> future )
    {
        DatabaseHandler.getInstance().loadFieldByKey( modelPrimaryKey, modelSchema, modelName, fieldName, this, () -> future.accept( value ) );
    }

    @Override
    public String getAsString()
    {
        return get().map( Object::toString ).orElse( "" );
    }

    @Override
    public void cache()
    {
        DatabaseHandler.getInstance().loadFieldByKey( modelPrimaryKey, modelSchema, modelName, fieldName, this );
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

    private void updated()
    {
        lastUpdated = System.currentTimeMillis();
    }

}
