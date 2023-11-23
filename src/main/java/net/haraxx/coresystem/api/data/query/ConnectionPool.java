package net.haraxx.coresystem.api.data.query;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Juyas
 * @version 23.11.2023
 * @since 23.11.2023
 */
interface ConnectionPool
{

    void close();

    Connection getConnection() throws SQLException;

}
