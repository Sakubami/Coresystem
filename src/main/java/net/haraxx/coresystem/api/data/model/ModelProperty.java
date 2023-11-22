package net.haraxx.coresystem.api.data.model;

import net.haraxx.coresystem.api.data.base.ColumnSettings;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 14.11.2023
 */
public interface ModelProperty<T>
{

    Value<T> value();

    Class<T> javaType();

    ColumnSettings settings();

}
