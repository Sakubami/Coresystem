package net.haraxx.coresystem.api.item;

import net.haraxx.coresystem.CoreSystem;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author Juyas
 * @version 11.11.2023
 * @since 11.11.2023
 */
public class NBT
{

    public record PersistentValue<T>(PersistentDataType<T, T> type, T data) {}

    private static final List<PersistentDataType<?, ?>> dataTypes = new ArrayList<>( 12 );
    private static final List<Class<?>> supportedClasses =
            List.of( String.class, Integer.class, int[].class, Double.class, Float.class, Short.class, Byte.class,
                    byte[].class, Long.class, long[].class, PersistentDataContainer.class, PersistentDataContainer[].class );

    static
    {
        dataTypes.add( PersistentDataType.STRING );
        dataTypes.add( PersistentDataType.INTEGER );
        dataTypes.add( PersistentDataType.INTEGER_ARRAY );
        dataTypes.add( PersistentDataType.DOUBLE );
        dataTypes.add( PersistentDataType.FLOAT );
        dataTypes.add( PersistentDataType.SHORT );
        dataTypes.add( PersistentDataType.BYTE );
        dataTypes.add( PersistentDataType.BYTE_ARRAY );
        dataTypes.add( PersistentDataType.LONG );
        dataTypes.add( PersistentDataType.LONG_ARRAY );
        dataTypes.add( PersistentDataType.TAG_CONTAINER );
        dataTypes.add( PersistentDataType.TAG_CONTAINER_ARRAY );
    }

    private final Map<String, PersistentValue<?>> valueMap;

    public NBT()
    {
        this.valueMap = new HashMap<>();
    }

    public static NamespacedKey key( String namespace )
    {
        return new NamespacedKey( CoreSystem.getInstance(), namespace );
    }

    public boolean supports( Class<?> clazz )
    {
        return supportedClasses.stream().anyMatch( c -> c.isAssignableFrom( clazz ) );
    }

    public <T> void setTag( String tag, Class<T> clazz, T value )
    {
        PersistentDataType<T, T> dataType = get( clazz );
        if ( dataType == null ) return;
        valueMap.put( tag, new PersistentValue<>( dataType, value ) );
    }

    public <T> T getTag( String tag, Class<T> clazz )
    {
        PersistentDataType<T, T> dataType = get( clazz );
        if ( dataType == null ) return null;
        return clazz.cast( valueMap.get( tag ).data );
    }

    public void from( Supplier<PersistentDataContainer> supplier )
    {
        from( supplier.get() );
    }

    public void from( PersistentDataContainer container )
    {
        container.getKeys().forEach( key ->
                dataTypes.forEach( type ->
                {
                    if ( container.has( key, type ) )
                        valueMap.put( key.getKey(), read( container, key, get( type.getComplexType() ) ) );
                } ) );
    }

    public void apply( Supplier<PersistentDataContainer> container )
    {
        apply( container.get() );
    }

    public void apply( PersistentDataContainer container )
    {
        valueMap.forEach( ( tag, value ) -> castedSet( container, tag, value ) );
    }

    // ---------------------------------- helper functions ----------------------------------

    private <T> PersistentDataType<T, T> get( Class<T> clazz )
    {
        for ( PersistentDataType<?, ?> type : dataTypes )
        {
            if ( type.getComplexType().isAssignableFrom( clazz ) )
                //noinspection unchecked
                return (PersistentDataType<T, T>) type;
        }
        return null;
    }

    private <T> void castedSet( PersistentDataContainer container, String tag, PersistentValue<T> value )
    {
        container.set( key( tag ), value.type, value.data );
    }

    private <T> PersistentValue<T> read( PersistentDataContainer container, NamespacedKey tag, PersistentDataType<T, T> dataType )
    {
        return new PersistentValue<>( dataType, container.get( tag, dataType ) );
    }

}
