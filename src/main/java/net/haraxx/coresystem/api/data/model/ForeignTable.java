package net.haraxx.coresystem.api.data.model;

import java.lang.annotation.*;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
@Target(ElementType.RECORD_COMPONENT)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignTable
{
    String table();
    String keyName();
}
