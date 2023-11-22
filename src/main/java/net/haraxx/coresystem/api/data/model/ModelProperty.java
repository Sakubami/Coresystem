package net.haraxx.coresystem.api.data.model;

import net.haraxx.coresystem.api.data.base.ColumnSettings;

/**
 * Describes a supertype for all properties defined in a model.
 *
 * @param <T> the type for this property is limited to all primitive wrappers and {@link String}
 *
 * @author Juyas
 * @version 22.11.2023
 * @see PrimaryKey
 * @see Column
 * @see DatabaseColumn
 * @since 14.11.2023
 */
public interface ModelProperty<T>
{

    /**
     * Gets the value container of this property
     *
     * @return the property's value
     */
    Value<T> value();

    /**
     * Gets the java type of this property
     *
     * @return the {@link Class} describing the type of this property
     */
    Class<T> javaType();

    /**
     * Gets the {@link ColumnSettings} for this property containing information for database handling
     *
     * @return the {@link ColumnSettings} of this property
     */
    ColumnSettings settings();

}
