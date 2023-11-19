package net.haraxx.coresystem.api.data;

import net.haraxx.coresystem.api.data.connector.StatementGenerator;

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

    private StatementGenerator connector;

    private DatabaseHandler()
    {

    }

    public static void loadConnector()
    {

    }

}
