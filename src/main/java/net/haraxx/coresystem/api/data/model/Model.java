package net.haraxx.coresystem.api.data.model;

import java.lang.annotation.*;

/**
 * Describes a model and defines its naming for the database backend.
 *
 * @author Juyas
 * @version 22.11.2023
 * @since 14.11.2023
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Model
{

    /**
     * The database schema or simply the name of the database.
     *
     * @return the database schema
     */
    String schema() default "haraxx";

    /**
     * The name of the table inside the database
     *
     * @return the name of the table
     */
    String table();

}
