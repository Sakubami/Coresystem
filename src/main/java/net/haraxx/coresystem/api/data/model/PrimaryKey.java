package net.haraxx.coresystem.api.data.model;

import net.haraxx.coresystem.api.data.ModelProperty;
import net.haraxx.coresystem.api.data.Value;

/**
 * @author Juyas
 * @version 14.11.2023
 * @since 14.11.2023
 */
public interface PrimaryKey<T> extends ModelProperty<T>
{

    Value<T> value();

}
