package net.haraxx.coresystem.api.data.query;

import net.haraxx.coresystem.api.data.base.ColumnSettings;
import net.haraxx.coresystem.api.data.base.DataModel;
import net.haraxx.coresystem.api.data.model.*;
import net.haraxx.coresystem.api.util.Try;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
public final class DatabaseHandler
{

    private static DatabaseHandler instance;

    public static DatabaseHandler getInstance()
    {
        if ( instance == null )
        {
            synchronized(DatabaseHandler.class)
            {
                instance = new DatabaseHandler();
            }
        }
        return instance;
    }

    private final SQLStatementGenerator statementGenerator;
    private SQLQueryHandler sqlQueryHandler;
    private ScheduledFuture<?> scheduledFuture;

    private DatabaseHandler()
    {
        this.statementGenerator = new SQLStatementGenerator();
    }

    public SQLQueryHandler getQueryHandler()
    {
        return sqlQueryHandler;
    }

    public void load( Properties properties )
    {
        if ( this.scheduledFuture != null ) return;
        this.sqlQueryHandler = new SQLQueryHandler( new SQLConnectionPool( properties ) );
        this.scheduledFuture = this.sqlQueryHandler.schedule();
    }

    public void shutdown( boolean force )
    {
        this.scheduledFuture.cancel( force );
        sqlQueryHandler.shutdown();
    }

    public void createSchema( String modelSchema )
    {
        String statement = this.statementGenerator.createSchema( modelSchema );
        this.sqlQueryHandler.query( statement );
    }

    public void createTable( DataModel model )
    {
        createTable( model.primaryKey(), model.modelSchema(), model.modelName(), model.columns().values().toArray( new Column[0] ) );
    }

    public void createTable( PrimaryKey key, String modelSchema, String model, Column<?>... properties )
    {
        String statement = this.statementGenerator.createTable( modelSchema, model, key,
                Arrays.stream( properties ).map( Column::settings ).toArray( ColumnSettings[]::new ) );
        this.sqlQueryHandler.query( statement );
    }

    public void loadKeyByField( PrimaryKey key, String modelSchema, String model, Column<?> property )
    {
        String statement = this.statementGenerator.requestKey( key, modelSchema, model, property );
        this.sqlQueryHandler.querySelection( statement, result ->
        {
            if ( result.next() )
                key.value().set( result.getLong( key.settings().columnName() ) );
        } );
    }

    public void deleteEntry( DataModel model )
    {
        deleteEntry( model.primaryKey(), model.modelSchema(), model.modelName() );
    }

    public void deleteEntry( PrimaryKey key, String modelSchema, String model )
    {
        String statement = this.statementGenerator.delete( modelSchema, model, key );
        this.sqlQueryHandler.queryUpdate( statement );
    }

    public void updateField( PrimaryKey key, String modelSchema, String model, String fieldName, CachedValue<?> value )
    {
        String statement = this.statementGenerator.update( fieldName, value, modelSchema, model, key );
        this.sqlQueryHandler.queryUpdate( statement );
    }

    public void insertModelAndUpdateKey( DataModel model )
    {
        insertModelAndUpdateKey( model.primaryKey(), model.modelSchema(), model.modelName(), model.columns().values().toArray( new Column[0] ) );
    }

    public void insertModelAndUpdateKey( PrimaryKey key, String modelSchema, String model, Column<?>... properties )
    {
        String insert = this.statementGenerator.put( modelSchema, model, properties );
        String keyRequest = this.statementGenerator.requestKeyPrecisely( key, modelSchema, model, properties );
        this.sqlQueryHandler.query( insert );
        this.sqlQueryHandler.querySelection( keyRequest, result ->
        {
            if ( result.next() ) key.value().set( result.getLong( key.settings().columnName() ) );
        } );
    }

    public void loadFieldsByKey( DataModel model )
    {
        loadFieldsByKey( model.primaryKey(), model.modelSchema(), model.modelName(), model.columns().values().toArray( new Column[0] ) );
    }

    public void loadFieldsByKey( PrimaryKey key, String modelSchema, String model, Column<?>... properties )
    {
        String statement = this.statementGenerator.request( modelSchema, model, key, properties );
        this.sqlQueryHandler.querySelection( statement, result ->
        {
            if ( result.next() )
                Arrays.stream( properties ).forEach( property -> loadProperty( property, result ) );
        } );
    }

    public void loadFieldByKey( PrimaryKey key, String modelSchema, String model, String fieldName, CachedValue<?> cachedValue )
    {
        String statement = this.statementGenerator.request( modelSchema, model, key, fieldName );
        this.sqlQueryHandler.querySelection( statement, result -> loadValue( fieldName, cachedValue, result ) );
    }

    public void loadFieldByKey( PrimaryKey key, String modelSchema, String model, String fieldName, CachedValue<?> cachedValue, Runnable confirmation )
    {
        String statement = this.statementGenerator.request( modelSchema, model, key, fieldName );
        this.sqlQueryHandler.querySelection( statement, result ->
        {
            loadValue( fieldName, cachedValue, result );
            confirmation.run();
        } );
    }

    private <T> void loadValue( String fieldName, CachedValue<T> value, ResultSet set )
    {
        value.set( Try.logRaw( () -> set.getObject( fieldName, value.type() ) ) );
    }

    private <T> void loadProperty( Column<T> column, ResultSet set )
    {
        column.value().set( Try.logRaw( () -> set.getObject( column.settings().columnName(), column.javaType() ) ) );
    }

}
