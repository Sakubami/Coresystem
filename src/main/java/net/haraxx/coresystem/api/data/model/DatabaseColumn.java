package net.haraxx.coresystem.api.data.model;

import net.haraxx.coresystem.api.data.base.ColumnSettings;

import java.lang.annotation.*;

/**
 * Provides meta data for a property in a model.
 *
 * @author Juyas
 * @version 22.11.2023
 * @see PrimaryKey
 * @see Column
 * @see ColumnSettings
 * @since 16.11.2023
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseColumn
{

    /**
     * The name of the sql column. It should satisfy the regex <code>^[a-zA-Z_][a-zA-Z0-9_]*$</code>
     *
     * @return the name of the sql column
     */
    String name();

    /**
     * The sql type of sql column. A list of valid types can be looked up <a href="https://www.w3schools.com/sql/sql_datatypes.asp">here</a>. <br>
     * Please note, that not all types are supported by {@link ModelProperty} and might therefore be unusable or usable through {@link String} on the java side only.
     *
     * @return the sql type of the sql column
     */
    String sqlType();

    /**
     * Whether the field is supposed to be never null when writing to the database.
     * default: {@link ColumnSettings#DEFAULT_NON_NULL}
     *
     * @return the not-null sql definition
     */
    boolean nonNull() default ColumnSettings.DEFAULT_NON_NULL;

    /**
     * Whether the field is supposed to be unique among its table.
     * default: {@link ColumnSettings#DEFAULT_UNIQUE}
     *
     * @return the unique sql definition
     */
    boolean unique() default ColumnSettings.DEFAULT_UNIQUE;

}
