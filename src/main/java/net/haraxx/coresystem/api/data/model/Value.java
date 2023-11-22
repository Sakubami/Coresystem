package net.haraxx.coresystem.api.data.model;

import java.util.Optional;

/**
 * A value container
 *
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
public interface Value<T>
{

    /**
     * The java type of this value container
     *
     * @return the {@link Class} describing the type of this value container
     */
    Class<T> type();

    /**
     * Gets an optional wrapped around the actual value
     *
     * @return an optional wrapped around the contained value
     */
    Optional<T> get();

    /**
     * Gets this value as string
     *
     * @return the value as string
     */
    String getAsString();

    /**
     * Overwrites the contained value with the given one
     *
     * @param value the new value
     */
    void set( T value );

}
