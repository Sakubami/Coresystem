package net.haraxx.coresystem.api.data.connector;

import net.haraxx.coresystem.api.data.impl.ColumnSettings;
import net.haraxx.coresystem.api.data.model.ModelProperty;
import net.haraxx.coresystem.api.data.model.PrimaryKey;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public class SQLStatementGenerator implements StatementGenerator
{

    private static final String CREATE = "CREATE TABLE";
    private static final String INSERT = "INSERT INTO";
    private static final String SELECT = "SELECT";
    private static final String UPDATE = "UPDATE";
    private static final String FROM = "FROM";
    private static final String VALUES = "VALUES";
    private static final String WHERE = "WHERE";
    private static final String SET = "SET";
    private static final String NOT_NULL = "NOT_NULL";
    private static final String UNIQUE = "UNIQUE";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String AUTO_INCREMENT = "AUTO_INCREMENT";

    @Override
    public String createTable( String model, PrimaryKey key, ColumnSettings... properties )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( CREATE ).append( " " ).append( model ).append( " (" );
        builder.append( key.settings().columnName() ).append( " " )
                .append( key.settings().sqlType() ).append( " " ).append( PRIMARY_KEY ).append( " " ).append( AUTO_INCREMENT );
        for ( ColumnSettings column : properties )
        {
            builder.append( ", " ).append( column.columnName() ).append( " " ).append( column.sqlType() );
            if ( column.nonNull() ) builder.append( " " ).append( UNIQUE );
            if ( column.unique() ) builder.append( " " ).append( NOT_NULL );
        }
        builder.append( ");" );
        return builder.toString();
    }

    @Override
    public String request( String model, PrimaryKey key, ModelProperty<?>... properties )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( SELECT ).append( " " );
        for ( ModelProperty<?> property : properties )
        {
            ColumnSettings column = property.settings();
            builder.append( column.columnName() ).append( ", " );
        }
        builder.delete( builder.length() - 3, builder.length() - 1 );
        builder.append( " " ).append( FROM ).append( " " ).append( model ).append( " " )
                .append( WHERE ).append( " " ).append( key.settings().columnName() ).append( "=" ).append( key.value().getAsString() );
        builder.append( ";" );
        return builder.toString();
    }

    @Override
    public <T> String requestKey( PrimaryKey key, String model, ModelProperty<T> property )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( SELECT ).append( " " ).append( key.settings().columnName() ).append( " " )
                .append( FROM ).append( " " ).append( model ).append( " " )
                .append( WHERE ).append( " " ).append( property.settings().columnName() ).append( "=" );
        //TODO implement checks for invalid characters in sql strings to prevent bugs
        if ( String.class.isAssignableFrom( property.javaType() ) )
            builder.append( "'" ).append( property.value().getAsString() ).append( "'" );
        else builder.append( property.value().getAsString() );
        builder.append( ";" );
        return builder.toString();
    }

    @Override
    public String update( ModelProperty<?> property, String model, PrimaryKey key )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( UPDATE ).append( " " ).append( model ).append( " " ).append( SET ).append( " " )
                .append( property.settings().columnName() ).append( "=" );
        if ( String.class.isAssignableFrom( property.javaType() ) )
            builder.append( "'" ).append( property.value().getAsString() ).append( "'" );
        else builder.append( property.value().getAsString() );
        builder.append( " " ).append( WHERE ).append( " " ).append( key.settings().columnName() )
                .append( "=" ).append( key.value().getAsString() ).append( ";" );
        return builder.toString();
    }

    @Override
    public String put( String model, PrimaryKey key, ModelProperty<?>... values )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( INSERT ).append( " " ).append( model ).append( " (" );
        for ( ModelProperty<?> property : values )
        {
            builder.append( property.settings().columnName() ).append( "," );
        }
        builder.deleteCharAt( builder.length() - 1 ).append( ") " );
        builder.append( VALUES ).append( " (" );
        for ( ModelProperty<?> property : values )
        {
            if ( String.class.isAssignableFrom( property.javaType() ) )
                builder.append( "'" ).append( property.value().getAsString() ).append( "'" );
            else builder.append( property.value().getAsString() );
            builder.append( "," );
        }
        builder.deleteCharAt( builder.length() - 1 ).append( ");" );
        return builder.toString();
    }

}
