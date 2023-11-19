package net.haraxx.coresystem.api.data.connector;

import net.haraxx.coresystem.api.util.Try;

import java.sql.*;
import java.util.Properties;

/**
 * @author Juyas
 * @version 19.11.2023
 * @since 19.11.2023
 */
public class SQLConnector
{

    private final String url;
    private final Properties connectionProps;
    private Connection connection;

    public SQLConnector( String url, String user, int password )
    {
        this.url = url;
        this.connectionProps = new Properties();
        this.connectionProps.put( "user", user );
        this.connectionProps.put( "password", password );
        this.connection = null;
    }

    public void ensureConnection()
    {
        if ( connection == null || Try.silent( () -> connection.isClosed(), true ) )
            Try.log( this::connect );
    }

    private void connect() throws SQLException
    {
        connection = DriverManager.getConnection( url, connectionProps );
    }

}
