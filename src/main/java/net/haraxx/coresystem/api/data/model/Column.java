package net.haraxx.coresystem.api.data.model;

/**
 * Describes a column for database handling. <br>
 * A model must only contain columns besides one {@link PrimaryKey}. <br>
 *
 * @author Juyas
 * @version 22.11.2023
 * @see ModelProperty
 * @see PrimaryKey
 * @see DatabaseColumn
 * @since 16.11.2023
 */
public interface Column<T> extends ModelProperty<T>
{

    /**
     * The {@link CachedValue} for the column providing database access.
     *
     * @return the {@link CachedValue}
     */
    CachedValue<T> value();

}
