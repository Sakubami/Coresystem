package net.haraxx.coresystem.api.data.model;

/**
 * Describes a primary key for database handling, is supposed to be unique among all table entries and must not be modified manually. <br>
 * A model must contain at least and at most a single {@link PrimaryKey}. <br>
 * The {@link PrimaryKey} is fixed as a {@link ModelProperty} with type parameter {@link Long}, but the sql type can still be defined freely as any valid integer-type number.
 *
 * @author Juyas
 * @version 14.11.2023
 * @see ModelProperty
 * @see Column
 * @see DatabaseColumn
 * @since 14.11.2023
 */
public interface PrimaryKey extends ModelProperty<Long>
{

}
