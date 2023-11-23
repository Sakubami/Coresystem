package net.haraxx.coresystem.api.data.query;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 20.11.2023
 */
final class SQLConnectionPool implements ConnectionPool
{

    private final HikariDataSource hikariDataSource;

    SQLConnectionPool( HikariConfig config )
    {
        this.hikariDataSource = new HikariDataSource( config );
    }

    SQLConnectionPool( Properties properties )
    {
        this( new HikariConfig( properties ) );
    }

    SQLConnectionPool( String jdbcUrl, String user, String password, int minimumIdle, int maxPoolSize )
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl( jdbcUrl );
        config.setUsername( user );
        config.setPassword( password );
        config.setMaximumPoolSize( maxPoolSize );
        config.setMinimumIdle( minimumIdle );
        config.setPoolName( "haraxx-sql-pool" );
        this.hikariDataSource = new HikariDataSource( config );
    }

    @Override
    public void close()
    {
        hikariDataSource.close();
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return hikariDataSource.getConnection();
    }

}
