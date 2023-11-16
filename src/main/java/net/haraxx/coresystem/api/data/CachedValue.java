package net.haraxx.coresystem.api.data;

import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * @author Juyas
 * @version 14.11.2023
 * @since 14.11.2023
 */
public interface CachedValue<T> extends Value<T>
{

    void get( Consumer<T> future );

    void updateCache();

    boolean isCached();

    LocalDateTime lastUpdated();

}
