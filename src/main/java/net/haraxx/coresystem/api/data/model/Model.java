package net.haraxx.coresystem.api.data.model;

import java.lang.annotation.*;

/**
 * @author Juyas
 * @version 14.11.2023
 * @since 14.11.2023
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Model
{

    String value();

}