package net.haraxx.coresystem.api.data;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public interface CachedModelProperty<T> extends ModelProperty<T>
{

    CachedValue<T> value();

}
