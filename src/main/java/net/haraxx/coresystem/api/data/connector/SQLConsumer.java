package net.haraxx.coresystem.api.data.connector;

import java.sql.SQLException;

/**
 * @author Juyas
 * @version 20.11.2023
 * @since 20.11.2023
 */
public interface SQLConsumer<T>
{

    void accept( T value ) throws SQLException;

}
