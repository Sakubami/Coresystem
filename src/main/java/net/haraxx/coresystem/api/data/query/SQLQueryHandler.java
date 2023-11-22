package net.haraxx.coresystem.api.data.query;

import net.haraxx.coresystem.CoreSystem;
import net.haraxx.coresystem.api.util.Try;

import java.sql.*;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.logging.Level;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 20.11.2023
 */
public final class SQLQueryHandler
{

    public static final long FREQUENCY = 1000L; //1-second period for queries to be pushed

    private final ScheduledExecutorService service;
    private final SQLConnectionPool connectionPool;
    private final SQLQueryExecutor sqlQueryExecutor;

    SQLQueryHandler( SQLConnectionPool connectionPool )
    {
        this.service = Executors.newSingleThreadScheduledExecutor();
        this.connectionPool = connectionPool;
        this.sqlQueryExecutor = new SQLQueryExecutor();
    }

    private Connection getConnection()
    {
        try
        {
            return connectionPool.getHikariDataSource().getConnection();
        }
        catch ( SQLException e )
        {
            CoreSystem.getInstance().getLogger().log( Level.SEVERE, "Could not establish database connection", e );
            return null;
        }
    }

    ScheduledFuture<?> schedule()
    {
        return service.scheduleAtFixedRate( this.sqlQueryExecutor, 0L, FREQUENCY, TimeUnit.MILLISECONDS );
    }

    void shutdown()
    {
        this.service.shutdown();
        connectionPool.getHikariDataSource().close();
    }

    /**
     * Commits a query statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement the sql statement
     */
    public void query( String statement )
    {
        query( statement, res -> {} );
    }

    /**
     * Commits a query statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement      the sql statement
     * @param resultConsumer provides a boolean result value as soon as the query has been executed
     */
    public void query( String statement, SQLConsumer<Boolean> resultConsumer )
    {
        sqlQueryExecutor.offerQuery( new SQLRequest<>( statement, resultConsumer ) );
    }

    /**
     * Commits an update statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement the sql statement
     */
    public void queryUpdate( String statement )
    {
        queryUpdate( statement, res -> {} );
    }

    /**
     * Commits an update statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement      the sql statement
     * @param resultConsumer provides an integer result value as soon as the query has been executed
     */
    public void queryUpdate( String statement, SQLConsumer<Integer> resultConsumer )
    {
        sqlQueryExecutor.offerQueryUpdate( new SQLRequest<>( statement, resultConsumer ) );
    }

    /**
     * Commits a selection statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement      the sql statement
     * @param resultConsumer provides {@link ResultSet} as soon as the query has been executed
     */
    public void querySelection( String statement, SQLConsumer<ResultSet> resultConsumer )
    {
        sqlQueryExecutor.offerQuerySelection( new SQLRequest<>( statement, resultConsumer ) );
    }

    private record SQLRequest<T>(String statement, SQLConsumer<T> processResult) {}

    private class SQLQueryExecutor implements Runnable
    {

        private final Queue<SQLRequest<Boolean>> queryQueue = new LinkedBlockingQueue<>();
        private final Queue<SQLRequest<Integer>> updateQueue = new LinkedBlockingQueue<>();
        private final Queue<SQLRequest<ResultSet>> selectionQueue = new LinkedBlockingQueue<>();

        @Override
        public void run()
        {
            Connection connection = getConnection();
            if ( connection == null ) return;
            workQuery( connection );
            workUpdates( connection );
            workSelections( connection );
            Try.silent( connection::close );
        }

        private void workQuery( Connection connection )
        {
            while ( !queryQueue.isEmpty() )
            {
                Statement statement = null;
                String sqlStatement = "";
                try
                {
                    SQLRequest<Boolean> sqlRequest = queryQueue.poll();
                    statement = connection.createStatement();
                    boolean executed = statement.execute( sqlStatement = sqlRequest.statement() );
                    sqlRequest.processResult.accept( executed );
                }
                catch ( Exception e )
                {
                    CoreSystem.getInstance().getLogger().log( Level.WARNING, "error while executing sql query: \"" + sqlStatement + "\"", e );
                }
                finally
                {
                    Statement finalStatement = statement;
                    Try.silent( () -> finalStatement.close() );
                }
            }
        }

        private void workUpdates( Connection connection )
        {
            while ( !updateQueue.isEmpty() )
            {
                Statement statement = null;
                String sqlStatement = "";
                try
                {
                    SQLRequest<Integer> sqlRequest = updateQueue.poll();
                    statement = connection.createStatement();
                    int updated = statement.executeUpdate( sqlStatement = sqlRequest.statement() );
                    sqlRequest.processResult.accept( updated );
                }
                catch ( Exception e )
                {
                    CoreSystem.getInstance().getLogger().log( Level.WARNING, "error while executing sql update query: \"" + sqlStatement + "\"", e );
                }
                finally
                {
                    Statement finalStatement = statement;
                    Try.silent( () -> finalStatement.close() );
                }
            }
        }

        private void workSelections( Connection connection )
        {
            while ( !selectionQueue.isEmpty() )
            {
                Statement statement = null;
                ResultSet resultSet = null;
                String sqlStatement = "";
                try
                {
                    SQLRequest<ResultSet> sqlRequest = selectionQueue.poll();
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery( sqlStatement = sqlRequest.statement() );
                    sqlRequest.processResult.accept( resultSet );
                }
                catch ( Exception e )
                {
                    CoreSystem.getInstance().getLogger().log( Level.WARNING, "error while executing sql update query: \"" + sqlStatement + "\"", e );
                }
                finally
                {
                    Try.silent( resultSet::close );
                    Try.silent( statement::close );
                }
            }
        }

        void offerQuery( SQLRequest<Boolean> request )
        {
            queryQueue.offer( request );
        }

        void offerQueryUpdate( SQLRequest<Integer> request )
        {
            updateQueue.offer( request );
        }

        void offerQuerySelection( SQLRequest<ResultSet> request )
        {
            selectionQueue.offer( request );
        }

    }

}
