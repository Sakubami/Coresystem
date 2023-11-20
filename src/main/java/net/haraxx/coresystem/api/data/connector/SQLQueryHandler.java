package net.haraxx.coresystem.api.data.connector;

import net.haraxx.coresystem.api.util.Try;

import java.sql.*;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author Juyas
 * @version 20.11.2023
 * @since 20.11.2023
 */
public class SQLQueryHandler
{

    private static final long PERIOD = 1000L; //1 second period

    private final ScheduledExecutorService service;
    private final SQLConnectionPool connectionPool;
    private final SQLQueryExecutor sqlQueryExecutor;

    public SQLQueryHandler( SQLConnectionPool connectionPool )
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
            //TODO logging
        }
        return null;
    }

    public ScheduledFuture<?> schedule()
    {
        return service.scheduleAtFixedRate( this.sqlQueryExecutor, 0L, PERIOD, TimeUnit.MILLISECONDS );
    }

    public void shutdown()
    {
        this.service.shutdown();
        connectionPool.getHikariDataSource().close();
    }

    public void query( String statement )
    {
        query( statement, res -> {} );
    }

    public void query( String statement, SQLConsumer<Boolean> resultConsumer )
    {
        sqlQueryExecutor.offerQuery( new SQLRequest<>( statement, resultConsumer ) );
    }

    public void queryUpdate( String statement )
    {
        queryUpdate( statement, res -> {} );
    }

    public void queryUpdate( String statement, SQLConsumer<Integer> resultConsumer )
    {
        sqlQueryExecutor.offerQueryUpdate( new SQLRequest<>( statement, resultConsumer ) );
    }

    public void querySelection( String statement, SQLConsumer<ResultSet> resultConsumer )
    {
        sqlQueryExecutor.offerQuerySelection( new SQLRequest<>( statement, resultConsumer ) );
    }

    private record SQLRequest<T>(String statement, SQLConsumer<T> processResult) {}

    class SQLQueryExecutor implements Runnable
    {

        private final Queue<SQLRequest<Boolean>> queryQueue = new LinkedBlockingQueue<>();
        private final Queue<SQLRequest<Integer>> updateQueue = new LinkedBlockingQueue<>();
        private final Queue<SQLRequest<ResultSet>> selectionQueue = new LinkedBlockingQueue<>();

        @Override
        public void run()
        {
            Connection connection = getConnection();
            workQuery( connection );
            workUpdates( connection );
            workSelections( connection );
            Try.silent( () -> connection.close() );
        }

        private void workQuery( Connection connection )
        {
            while ( !queryQueue.isEmpty() )
            {
                Statement statement = null;
                try
                {
                    SQLRequest<Boolean> sqlRequest = queryQueue.poll();
                    statement = connection.createStatement();
                    boolean executed = statement.execute( sqlRequest.statement() );
                    sqlRequest.processResult.accept( executed );
                }
                catch ( Exception ignored )
                {
                    //TODO logging
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
                try
                {
                    SQLRequest<Integer> sqlRequest = updateQueue.poll();
                    statement = connection.createStatement();
                    int updated = statement.executeUpdate( sqlRequest.statement() );
                    sqlRequest.processResult.accept( updated );
                }
                catch ( Exception ignored )
                {
                    //TODO logging
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
                try
                {
                    SQLRequest<ResultSet> sqlRequest = selectionQueue.poll();
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery( sqlRequest.statement() );
                    sqlRequest.processResult.accept( resultSet );
                }
                catch ( Exception ignored )
                {
                    //TODO logging
                }
                finally
                {
                    ResultSet finalResultSet = resultSet;
                    Try.silent( () -> finalResultSet.close() );
                    Statement finalStatement = statement;
                    Try.silent( () -> finalStatement.close() );
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
