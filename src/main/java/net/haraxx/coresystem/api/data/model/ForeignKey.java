package net.haraxx.coresystem.api.data.model;

import net.haraxx.coresystem.api.data.CachedModelProperty;

/**
 * @author Juyas
 * @version 14.11.2023
 * @since 14.11.2023
 */
public interface ForeignKey<T> extends CachedModelProperty<T>
{

    String foreignTable();

    String foreignKeyName();

}
