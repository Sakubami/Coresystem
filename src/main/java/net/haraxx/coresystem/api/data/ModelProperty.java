package net.haraxx.coresystem.api.data;

/**
 * @author Juyas
 * @version 14.11.2023
 * @since 14.11.2023
 */
public interface ModelProperty<T>
{

    Value<T> value();

    Class<T> javaType();

    ColumnSettings settings();

}
