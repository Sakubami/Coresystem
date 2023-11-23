package net.haraxx.coresystem.api.data.query;

import net.haraxx.coresystem.api.data.base.ColumnSettings;
import net.haraxx.coresystem.api.data.model.*;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
class SQLStatementGenerator
{

    private static final String NOT_NULL = "NOT NULL";
    private static final String UNIQUE = "UNIQUE";
    private static final String PRIMARY_KEY_FLAGS = "PRIMARY KEY AUTO_INCREMENT";

    private static final String CREATE_SCHEMA = "IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = {0}) CREATE SCHEMA {1};";
    private static final String CREATE_TABLE = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = {0} AND TABLE_NAME = {1}) CREATE TABLE {2} ({3});";
    private static final String DELETE = "DELETE FROM {0} WHERE {1};";
    private static final String SELECT = "SELECT {0} FROM {1} WHERE {2};";
    private static final String UPDATE = "UPDATE {0} SET {1} WHERE {2};";
    private static final String INSERT = "INSERT INTO {0} ({1}) VALUES ({2});";

    private String quote( String val )
    {
        return "N'" + val + "'";
    }

    private String list( List<String> elements, String delim )
    {
        StringBuilder builder = new StringBuilder();
        for ( int i = 0; i < elements.size() - 1; i++ )
        {
            builder.append( elements.get( i ) ).append( delim );
        }
        builder.append( elements.get( elements.size() - 1 ) );
        return builder.toString();
    }

    private List<String> listNamesAndTypes( ColumnSettings... properties )
    {
        return Arrays.stream( properties ).map( column ->
        {
            String elem = column.columnName() + " " + column.sqlType();
            if ( column.unique() ) elem += " " + UNIQUE;
            if ( column.nonNull() ) elem += " " + NOT_NULL;
            return elem;
        } ).toList();
    }

    private List<String> listValues( Column<?>... properties )
    {
        return Arrays.stream( properties )
                .map( column -> column.settings().columnName() + "=" + quoteConditional( column ) )
                .toList();
    }

    private String listNames( Column<?>... properties )
    {
        return list( Arrays.stream( properties ).map( column -> column.settings().columnName() ).toList(), "," );
    }

    private String listRawValues( Column<?>... properties )
    {
        return list( Arrays.stream( properties ).map( this::quoteConditional ).toList(), "," );
    }

    private String quoteConditional( Column<?> value )
    {
        return quoteConditional( value.value() );
    }

    private String quoteConditional( Value<?> value )
    {
        if ( String.class.isAssignableFrom( value.type() ) )
            return quote( value.getAsString() );
        else return value.getAsString();
    }

    private String keyCondition( PrimaryKey key )
    {
        return key.settings().columnName() + "=" + key.value().getAsString();
    }

    String createSchema( String modelSchema )
    {
        return MessageFormat.format( CREATE_SCHEMA, quote( modelSchema ), modelSchema );
    }

    String createTable( String modelSchema, String model, PrimaryKey key, ColumnSettings... properties )
    {
        String primKey = key.settings().columnName() + " " + key.settings().sqlType() + " " + PRIMARY_KEY_FLAGS;
        List<String> list = new ArrayList<>( listNamesAndTypes( properties ) );
        list.add( 0, primKey );
        return MessageFormat.format( CREATE_TABLE, quote( modelSchema ), quote( model ), modelSchema + "." + model, list( list, "," ) );
    }

    String delete( String modelSchema, String model, PrimaryKey key )
    {
        return MessageFormat.format( DELETE, modelSchema + "." + model, keyCondition( key ) );
    }

    String request( String modelSchema, String model, PrimaryKey key, Column<?>... properties )
    {
        return MessageFormat.format( SELECT, listNames( properties ), modelSchema + "." + model, keyCondition( key ) );
    }

    String request( String modelSchema, String model, PrimaryKey key, String field )
    {
        return MessageFormat.format( SELECT, field, modelSchema + "." + model, keyCondition( key ) );
    }

    String requestKey( PrimaryKey key, String modelSchema, String model, Column<?> property )
    {
        return MessageFormat.format( SELECT, key.settings().columnName(), modelSchema + "." + model, property.settings().columnName() + "=" + quoteConditional( property ) );
    }

    String requestKeyPrecisely( PrimaryKey key, String modelSchema, String model, Column<?>... properties )
    {
        return MessageFormat.format( SELECT, key.settings().columnName(), modelSchema + "." + model, list( listValues( properties ), " AND " ) );
    }

    String update( String fieldName, CachedValue<?> value, String modelSchema, String model, PrimaryKey key )
    {
        return MessageFormat.format( UPDATE, modelSchema + "." + model, fieldName + "=" + quoteConditional( value ), keyCondition( key ) );
    }

    String put( String modelSchema, String model, Column<?>... values )
    {
        return MessageFormat.format( INSERT, modelSchema + "." + model, listNames( values ), listRawValues( values ) );
    }

}
