package net.haraxx.coresystem.api.data.query;

import net.haraxx.coresystem.api.data.base.ColumnSettings;
import net.haraxx.coresystem.api.data.model.*;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
class SQLStatementGenerator
{

    private static final String CREATE = "CREATE TABLE";
    private static final String CREATE_SCHEMA = "CREATE SCHEMA";
    private static final String INSERT = "INSERT INTO";
    private static final String SELECT = "SELECT";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private static final String FROM = "FROM";
    private static final String VALUES = "VALUES";
    private static final String WHERE = "WHERE";
    private static final String SET = "SET";
    private static final String AND = "AND";
    private static final String NOT_NULL = "NOT_NULL";
    private static final String UNIQUE = "UNIQUE";
    private static final String TERMINAL = ";";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String AUTO_INCREMENT = "AUTO_INCREMENT";

    String createSchema( String modelSchema )
    {
        String condition = "IF NOT EXISTS (SELECT * FROM sys.schemas WHERE name = N'" + modelSchema + "')";
        return condition + " " + CREATE_SCHEMA + " " + modelSchema + TERMINAL;
    }

    String createTable( String modelSchema, String model, PrimaryKey key, ColumnSettings... properties )
    {
        StringBuilder builder = new StringBuilder();
        String condition = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = N'" + modelSchema + "' AND TABLE_NAME = N'" + model + "')";
        builder.append( condition ).append( " " );
        builder.append( CREATE ).append( " " ).append( modelSchema ).append( "." ).append( model ).append( " (" );
        builder.append( key.settings().columnName() ).append( " " )
                .append( key.settings().sqlType() ).append( " " ).append( PRIMARY_KEY ).append( " " ).append( AUTO_INCREMENT );
        for ( ColumnSettings column : properties )
        {
            builder.append( ", " ).append( column.columnName() ).append( " " ).append( column.sqlType() );
            if ( column.nonNull() ) builder.append( " " ).append( UNIQUE );
            if ( column.unique() ) builder.append( " " ).append( NOT_NULL );
        }
        builder.append( ")" ).append( TERMINAL );
        return builder.toString();
    }

    String delete( String modelSchema, String model, PrimaryKey key )
    {
        return DELETE + " " + FROM + " " + modelSchema + "." + model + " " + WHERE + " " + key.settings().columnName() + "=" + key.value().getAsString() + TERMINAL;
    }

    String request( String modelSchema, String model, PrimaryKey key, Column<?>... properties )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( SELECT ).append( " " );
        for ( ModelProperty<?> property : properties )
        {
            ColumnSettings column = property.settings();
            builder.append( column.columnName() ).append( ", " );
        }
        builder.delete( builder.length() - 3, builder.length() - 1 );
        builder.append( " " ).append( FROM ).append( " " ).append( modelSchema ).append( "." ).append( model ).append( " " )
                .append( WHERE ).append( " " ).append( key.settings().columnName() ).append( "=" ).append( key.value().getAsString() );
        builder.append( TERMINAL );
        return builder.toString();
    }

    String request( String modelSchema, String model, PrimaryKey key, String field )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( SELECT ).append( " " ).append( field );
        builder.append( " " ).append( FROM ).append( " " ).append( modelSchema ).append( "." ).append( model ).append( " " )
                .append( WHERE ).append( " " ).append( key.settings().columnName() ).append( "=" ).append( key.value().getAsString() );
        builder.append( TERMINAL );
        return builder.toString();
    }

    String requestKey( PrimaryKey key, String modelSchema, String model, Column<?> property )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( SELECT ).append( " " ).append( key.settings().columnName() ).append( " " )
                .append( FROM ).append( " " ).append( modelSchema ).append( "." ).append( model ).append( " " )
                .append( WHERE ).append( " " ).append( property.settings().columnName() ).append( "=" );
        //TODO implement checks for invalid characters in sql strings to prevent bugs
        if ( String.class.isAssignableFrom( property.javaType() ) )
            builder.append( "N'" ).append( property.value().getAsString() ).append( "'" );
        else builder.append( property.value().getAsString() );
        builder.append( TERMINAL );
        return builder.toString();
    }

    String requestKeyPrecisely( PrimaryKey key, String modelSchema, String model, Column<?>... properties )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( SELECT ).append( " " ).append( key.settings().columnName() ).append( " " )
                .append( FROM ).append( " " ).append( modelSchema ).append( "." ).append( model ).append( " " )
                .append( WHERE ).append( " " );
        for ( ModelProperty<?> property : properties )
        {
            builder.append( property.settings().columnName() ).append( "=" );
            if ( String.class.isAssignableFrom( property.javaType() ) )
                builder.append( "N'" ).append( property.value().getAsString() ).append( "'" );
            else builder.append( property.value().getAsString() );
            builder.append( " " ).append( AND );
        }
        builder.deleteCharAt( builder.length() - AND.length() - 1 );
        builder.append( TERMINAL );
        return builder.toString();
    }

    String update( String fieldName, CachedValue<?> value, String modelSchema, String model, PrimaryKey key )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( UPDATE ).append( " " ).append( modelSchema ).append( "." ).append( model ).append( " " ).append( SET ).append( " " )
                .append( fieldName ).append( "=" );
        if ( String.class.isAssignableFrom( value.type() ) )
            builder.append( "N'" ).append( value.getAsString() ).append( "'" );
        else builder.append( value.getAsString() );
        builder.append( " " ).append( WHERE ).append( " " ).append( key.settings().columnName() )
                .append( "=" ).append( key.value().getAsString() ).append( TERMINAL );
        return builder.toString();
    }

    String put( String modelSchema, String model, Column<?>... values )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( INSERT ).append( " " ).append( modelSchema ).append( "." ).append( model ).append( " (" );
        for ( ModelProperty<?> property : values )
        {
            builder.append( property.settings().columnName() ).append( "," );
        }
        builder.deleteCharAt( builder.length() - 1 ).append( ") " );
        builder.append( VALUES ).append( " (" );
        for ( ModelProperty<?> property : values )
        {
            if ( String.class.isAssignableFrom( property.javaType() ) )
                builder.append( "N'" ).append( property.value().getAsString() ).append( "'" );
            else builder.append( property.value().getAsString() );
            builder.append( "," );
        }
        builder.deleteCharAt( builder.length() - 1 ).append( ")" ).append( TERMINAL );
        return builder.toString();
    }

}
