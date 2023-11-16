package net.haraxx.coresystem.api.data.model;

import net.haraxx.coresystem.api.data.ColumnSettings;

import java.lang.annotation.*;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
@Target(ElementType.RECORD_COMPONENT)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseColumn
{

    String name();

    String sqlType();

    boolean nonNull() default ColumnSettings.DEFAULT_NON_NULL;

    boolean unique() default ColumnSettings.DEFAULT_UNIQUE;

}
