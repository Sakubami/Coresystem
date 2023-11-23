package net.haraxx.coresystem.api.data.query;

import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

/**
 * @author Juyas
 * @version 23.11.2023
 * @since 23.11.2023
 */
public class SQLQueryHandlerTest
{

    private static final int TOLERANCE = 50; //milliseconds tolerance for sql query delay

    private DummyConnection connection;
    private ConnectionPool pool;
    private Vector<String> recentCalls;

    private SQLQueryHandler handler;

    private ScheduledFuture<?> future;

    @Before
    public void setUp() throws Exception
    {
        recentCalls = new Vector<>();
        BiConsumer<DummyStatement.QueryType, String> consumer = ( type, string ) -> recentCalls.add( type.name() + ">" + string );
        connection = new DummyConnection( consumer );
        pool = new ConnectionPool()
        {
            @Override
            public void close()
            {

            }

            @Override
            public Connection getConnection() throws SQLException
            {
                return connection;
            }
        };
        this.handler = new SQLQueryHandler( pool );
        this.future = this.handler.schedule();
    }

    @After
    public void shutdown()
    {
        this.future.cancel( false );
    }

    @Test
    public void query()
    {
        recentCalls = new Vector<>();
        BooleanLock lock = new BooleanLock();
        handler.query( "Thats dumb", res -> lock.unlock() );
        lock.join();
        Assert.assertEquals( DummyStatement.QueryType.RAW + ">" + "Thats dumb", recentCalls.firstElement() );
        Assert.assertTrue( "query handler took too long", lock.getLockedDuration() < SQLQueryHandler.FREQUENCY + TOLERANCE );
    }

    @Test
    public void queryUpdate()
    {
        recentCalls = new Vector<>();
        BooleanLock lock = new BooleanLock();
        handler.queryUpdate( "Thats dumb", res -> lock.unlock() );
        lock.join();
        Assert.assertEquals( DummyStatement.QueryType.UPDATE + ">" + "Thats dumb", recentCalls.firstElement() );
        Assert.assertTrue( "query handler took too long", lock.getLockedDuration() < SQLQueryHandler.FREQUENCY + TOLERANCE );
    }


    @Test
    public void querySelection()
    {
        recentCalls = new Vector<>();
        BooleanLock lock = new BooleanLock();
        handler.querySelection( "Thats dumb", res -> lock.unlock() );
        lock.join();
        Assert.assertEquals( DummyStatement.QueryType.QUERY + ">" + "Thats dumb", recentCalls.firstElement() );
        Assert.assertTrue( "query handler took too long", lock.getLockedDuration() < SQLQueryHandler.FREQUENCY + TOLERANCE );
    }

    @Test
    public void queryOrder()
    {
        //ensure to start right after a cycle to prevent mid-cycle calls breaking the test
        BooleanLock lock2 = new BooleanLock();
        handler.querySelection( "Thats dumb", res -> lock2.unlock() );
        lock2.join();

        int calls = 150; //this many calls should be easily possible within the timeframe
        recentCalls = new Vector<>();
        BooleanLock lock = new BooleanLock();
        AtomicReference<String> text = new AtomicReference<>("");
        Runnable[] queries = new Runnable[] {
                () -> {
                    handler.query( "Thats dumb", res -> text.getAndUpdate( s -> s + "r" ));
                },
                () -> {
                    handler.queryUpdate( "Thats dumb", res -> text.getAndUpdate( s -> s + "u" ) );
                },
                () -> {
                    handler.querySelection( "Thats dumb", res -> text.getAndUpdate( s -> s + "s" ) );
                }
        };
        int[] called = new int[3];
        Random random = new Random();
        for ( int i = 0; i < calls - 1; i++ )
        {
            int index = random.nextInt( 3 );
            queries[index].run();
            called[index]++;
        }
        handler.querySelection( "dummy", res -> lock.unlock() );
        lock.join();
        String expect = "r".repeat( called[0] ) + "u".repeat( called[1] ) + "s".repeat( called[2] );
        Assert.assertEquals( expect, text.get() );
        Assert.assertTrue( "query handler took too long", lock.getLockedDuration() < SQLQueryHandler.FREQUENCY + TOLERANCE );
    }

    static class BooleanLock
    {

        private final AtomicBoolean lock = new AtomicBoolean( true );

        private long creation = System.currentTimeMillis();
        private long unlocking = System.currentTimeMillis();

        void start()
        {
            creation = System.currentTimeMillis();
        }

        void unlock()
        {
            lock.set( false );
            unlocking = System.currentTimeMillis();
        }

        long getLockedDuration()
        {
            return unlocking - creation;
        }

        void join()
        {
            int counter = 0;
            try
            {
                while ( lock.get() && counter < 300 )
                {
                    Thread.sleep( 10 );
                    counter++;
                }
            }
            catch ( Exception ignored )
            {
            }
        }

    }
}