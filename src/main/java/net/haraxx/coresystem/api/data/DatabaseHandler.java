package net.haraxx.coresystem.api.data;

import net.haraxx.coresystem.api.data.connector.*;
import net.haraxx.coresystem.api.data.model.ModelProperty;
import net.haraxx.coresystem.api.data.model.PrimaryKey;

import java.util.Properties;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Juyas
 * @version 16.11.2023
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

    public void loadKeyByField( PrimaryKey key, String model, ModelProperty<?> property )
    {
        String statement = this.statementGenerator.requestKey( key, model, property );
        this.sqlQueryHandler.querySelection( statement, result ->
        {
            if ( result.next() )
                key.value().set( result.getLong( key.settings().columnName() ) );
        } );
    }

    public void updateField( PrimaryKey key, String model, ModelProperty<?> property )
    {
        String statement = this.statementGenerator.update( property, model, key );
        this.sqlQueryHandler.queryUpdate( statement );
    }

    public void loadFieldsByKey( PrimaryKey key, String model, ModelProperty<?>... properties )
    {
        String statement = this.statementGenerator.request( model, key, properties );
        this.sqlQueryHandler.querySelection( statement, result ->
        {
            //TODO
        } );
    }

}
