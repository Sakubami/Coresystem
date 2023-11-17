package net.haraxx.coresystem.api.data;

import net.haraxx.coresystem.api.data.model.*;

import java.util.Optional;

/**
 * @author Juyas
 * @version 14.11.2023
 * @since 14.11.2023
 */
final class ModelProperties<T>
{

    private final String column;
    private final Class<T> javaClass;
    private final String sqlType;
    private final boolean unique;
    private final boolean nonNull;

    private ModelProperties( DatabaseColumn settings, Class<T> javaClass )
    {
        this.column = settings.name();
        this.javaClass = javaClass;
        this.sqlType = settings.sqlType();
        this.unique = settings.unique();
        this.nonNull = settings.nonNull();
    }

    public Column<T> build( String model, PrimaryKey<?> primaryKey )
    {
        return new ModelPropertyBase<>( new ColumnSettings( column, sqlType, nonNull, unique ), javaClass,
                new DatabaseCachedValue<>( model, primaryKey ) );
    }

    public PrimaryKey<T> buildPrimaryKey()
    {
        return new PrimaryKeyBase<>( new ColumnSettings( column, sqlType, nonNull, unique ), javaClass, new DatabaseValue<>() );
    }

    public ForeignKey<T> buildForeignKey( String model, PrimaryKey<?> primaryKey, String foreignTable, String foreignKeyName )
    {
        return new ForeignKeyBase<>( new ColumnSettings( column, sqlType, nonNull, unique ), javaClass,
                new DatabaseCachedValue<>( model, primaryKey ), foreignTable, foreignKeyName );
    }

    public static <T> ModelProperties<T> create( Class<T> type, DatabaseColumn settings )
    {
        return new ModelProperties<>( settings, type );
    }

    private record ModelPropertyBase<T>(ColumnSettings settings, Class<T> javaType,
                                        CachedValue<T> value) implements Column<T> {}

    private record PrimaryKeyBase<T>(ColumnSettings settings, Class<T> javaType,
                                     Value<T> value) implements PrimaryKey<T> {}

    private record ForeignKeyBase<T>(ColumnSettings settings, Class<T> javaType,
                                     CachedValue<T> value, String foreignTable,
                                     String foreignKeyName) implements ForeignKey<T> {}

    private static final class DatabaseValue<T> implements Value<T>
    {

        private T value;

        @Override
        public Optional<T> get()
        {
            return Optional.ofNullable( value );
        }

        @Override
        public void update( T value )
        {
            this.value = value;
        }

    }

}
