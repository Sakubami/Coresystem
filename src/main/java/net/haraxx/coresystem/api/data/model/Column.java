package net.haraxx.coresystem.api.data.model;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public interface Column<T> extends ModelProperty<T>
{

    CachedValue<T> value();

}
