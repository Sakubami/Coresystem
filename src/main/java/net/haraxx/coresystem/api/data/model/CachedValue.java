package net.haraxx.coresystem.api.data.model;

import java.util.function.Consumer;

/**
 * A cached value container potentially usable with a database connection.
 *
 * @author Juyas
 * @version 22.11.2023
 * @see Value
 * @since 14.11.2023
 */
public interface CachedValue<T> extends Value<T>
{

    /**
     * Retrieves the value using a consumer after updating the cache.
     *
     * @param future the consumer to pass the freshly updated value to
     */
    void get( Consumer<T> future );

    /**
     * Updates the backend with potential recent changes made to this value by {@link #set(Object)}
     */
    void update();

    /**
     * Loads the cache of this value container from its backend
     */
    void cache();

    /**
     * Checks whether this value is present in cache. The call is equivalent to <code>get().isEmpty()</code>
     *
     * @return true, if and only if a value is present
     */
    boolean isCached();

    /**
     * Gets the last cache update time in milliseconds. Comparable values can be retrieved by {@link System#currentTimeMillis()}.
     *
     * @return the last time in milliseconds the cache has been updated
     */
    long lastUpdated();

}
