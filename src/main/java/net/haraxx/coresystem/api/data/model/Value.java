package net.haraxx.coresystem.api.data.model;

import java.util.Optional;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
public interface Value<T>
{

    Class<T> type();

    Optional<T> get();

    String getAsString();

    void set( T value );

}
