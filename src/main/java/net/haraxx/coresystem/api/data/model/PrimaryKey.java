package net.haraxx.coresystem.api.data.model;

/**
 * @author Juyas
 * @version 14.11.2023
 * @since 14.11.2023
 */
public interface PrimaryKey extends ModelProperty<Long>
{

    Value<Long> value();

}
