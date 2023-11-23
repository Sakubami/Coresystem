package net.haraxx.coresystem.api.data.query;

import net.haraxx.coresystem.CoreSystem;
import net.haraxx.coresystem.api.util.Try;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;

/**
 * @author Juyas
 * @version 23.11.2023
 * @since 20.11.2023
 */
public final class SQLQueryHandler
{

    public static final long FREQUENCY = 1000L; //1-second period for queries to be pushed

    private final ScheduledExecutorService service;
    private final ConnectionPool connectionPool;
    private final SQLQueryExecutor sqlQueryExecutor;

    SQLQueryHandler( ConnectionPool connectionPool )
    {
        this.service = Executors.newSingleThreadScheduledExecutor();
        this.connectionPool = connectionPool;
        this.sqlQueryExecutor = new SQLQueryExecutor();
    }

    private Connection getConnection()
    {
        try
        {
            return connectionPool.getConnection();
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
        connectionPool.close();
    }

    /**
     * Commits a query statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement the sql statement
     */
    public void query( @Nonnull String statement )
    {
        query( statement, res -> {} );
    }

    /**
     * Commits a query statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement      the sql statement
     * @param resultConsumer provides a boolean result value as soon as the query has been executed
     */
    public void query( @Nonnull String statement, SQLConsumer<Boolean> resultConsumer )
    {
        sqlQueryExecutor.offerQuery( new SQLRequest<>( statement, resultConsumer, SQLRequestType.EXECUTE ) );
    }

    /**
     * Commits an update statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement the sql statement
     */
    public void queryUpdate( @Nonnull String statement )
    {
        queryUpdate( statement, res -> {} );
    }

    /**
     * Commits an update statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement      the sql statement
     * @param resultConsumer provides an integer result value as soon as the query has been executed
     */
    public void queryUpdate( @Nonnull String statement, SQLConsumer<Integer> resultConsumer )
    {
        sqlQueryExecutor.offerQuery( new SQLRequest<>( statement, resultConsumer, SQLRequestType.UPDATE ) );
    }

    /**
     * Commits a selection statement to the connection pool to be pushed to the database within {@link #FREQUENCY} milliseconds.
     *
     * @param statement      the sql statement
     * @param resultConsumer provides {@link ResultSet} as soon as the query has been executed
     */
    public void querySelection( @Nonnull String statement, SQLConsumer<ResultSet> resultConsumer )
    {
        sqlQueryExecutor.offerQuery( new SQLRequest<>( statement, resultConsumer, SQLRequestType.SELECT ) );
    }

    private record SQLRequest<T>(@Nonnull String statement, SQLConsumer<T> processResult, SQLRequestType type) {}

    public enum SQLRequestType
    {
        EXECUTE,
        UPDATE,
        SELECT
    }

    private class SQLQueryExecutor implements Runnable
    {

        private final Queue<SQLRequest<?>> prioQueue = new PriorityQueue<>( 200, Comparator.comparing( SQLRequest::type ) );

        @Override
        public void run()
        {
            Connection connection = getConnection();
            if ( connection == null ) return;
            while ( !prioQueue.isEmpty() )
            {
                switch ( prioQueue.peek().type() )
                {
                    case EXECUTE ->
                    {
                        workQuery( connection );
                    }
                    case UPDATE ->
                    {
                        workUpdate( connection );
                    }
                    case SELECT ->
                    {
                        workSelection( connection );
                    }
                }
            }
            Try.silent( connection::close );
        }

        private void workQuery( Connection connection )
        {
            Statement statement = null;
            String sqlStatement = "";
            try
            {
                @SuppressWarnings("unchecked")
                SQLRequest<Boolean> sqlRequest = (SQLRequest<Boolean>) prioQueue.poll();
                statement = connection.createStatement();
                boolean executed = statement.execute( sqlStatement = sqlRequest.statement() );
                sqlRequest.processResult.accept( executed );
            }
            catch ( Exception e )
            {
                CoreSystem.getInstance().getLogger().log( Level.WARNING, "error while executing sql execution query: \"" + sqlStatement + "\"", e );
            }
            finally
            {
                if ( statement != null ) Try.silent( statement::close );
            }
        }

        private void workUpdate( Connection connection )
        {
            Statement statement = null;
            String sqlStatement = "";
            try
            {
                @SuppressWarnings("unchecked")
                SQLRequest<Integer> sqlRequest = (SQLRequest<Integer>) prioQueue.poll();
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
                if ( statement != null ) Try.silent( statement::close );
            }
        }

        private void workSelection( Connection connection )
        {
            Statement statement = null;
            ResultSet resultSet = null;
            String sqlStatement = "";
            try
            {
                @SuppressWarnings("unchecked")
                SQLRequest<ResultSet> sqlRequest = (SQLRequest<ResultSet>) prioQueue.poll();
                statement = connection.createStatement();
                resultSet = statement.executeQuery( sqlStatement = sqlRequest.statement() );
                sqlRequest.processResult.accept( resultSet );
            }
            catch ( Exception e )
            {
                CoreSystem.getInstance().getLogger().log( Level.WARNING, "error while executing sql selection query: \"" + sqlStatement + "\"", e );
            }
            finally
            {
                if ( resultSet != null ) Try.silent( resultSet::close );
                if ( statement != null ) Try.silent( statement::close );
            }
        }

        void offerQuery( SQLRequest<?> request )
        {
            prioQueue.offer( request );
        }

    }

}
