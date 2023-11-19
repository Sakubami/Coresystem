package net.haraxx.coresystem.api.data.model;

import java.util.Optional;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public interface Value<T>
{

    Optional<T> get();

    String getAsString();

    void update( T value );

}
