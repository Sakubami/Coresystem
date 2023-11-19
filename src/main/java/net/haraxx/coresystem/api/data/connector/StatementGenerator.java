package net.haraxx.coresystem.api.data.connector;

import net.haraxx.coresystem.api.data.impl.ColumnSettings;
import net.haraxx.coresystem.api.data.model.ModelProperty;
import net.haraxx.coresystem.api.data.model.PrimaryKey;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public interface StatementGenerator
{

    String createTable( String model, PrimaryKey key, ColumnSettings... properties );

    String request( String model, PrimaryKey key, ModelProperty<?>... properties );

    <T> String requestKey( PrimaryKey key, String model, ModelProperty<T> property );

    String update( ModelProperty<?> property, String model, PrimaryKey key );

    String put( String mode, PrimaryKey key, ModelProperty<?>... values );

}
