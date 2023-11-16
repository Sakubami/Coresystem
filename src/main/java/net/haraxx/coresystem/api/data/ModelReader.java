package net.haraxx.coresystem.api.data;

import net.haraxx.coresystem.api.data.model.*;

import java.lang.reflect.*;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public final class ModelReader
{

    public static <T> T buildModel( Class<T> clazz ) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException
    {
        //TODO more solid argument checking, exceptions and control flow handling
        if ( !clazz.isRecord() || !clazz.isAnnotationPresent( Model.class ) ) return null;
        Model model = clazz.getDeclaredAnnotation( Model.class );
        RecordComponent[] components = clazz.getRecordComponents();
        Class<?>[] constructorParams = new Class[components.length];
        Class<?>[] constructorParamGenerics = new Class[components.length];
        DatabaseColumn[] columnarData = new DatabaseColumn[components.length];
        ForeignTable[] foreignKeys = new ForeignTable[components.length];
        int primaryKey = -1;
        for ( int i = 0; i < components.length; i++ )
        {
            RecordComponent component = components[i];
            //check for model property types - all of them require the annotation
            Class<?> type = component.getType();
            if ( !ModelProperty.class.isAssignableFrom( type ) ) return null;
            //note down the primary key
            if ( PrimaryKey.class.isAssignableFrom( type ) )
            {
                //only one primary key valid
                if ( primaryKey != -1 ) return null;
                primaryKey = i;
            }
            if ( ForeignKey.class.isAssignableFrom( type ) )
            {
                //read out foreign key data
                if ( !component.isAnnotationPresent( ForeignTable.class ) ) return null;
                foreignKeys[i] = component.getDeclaredAnnotation( ForeignTable.class );
            }
            if ( !component.isAnnotationPresent( DatabaseColumn.class ) ) return null;
            columnarData[i] = component.getDeclaredAnnotation( DatabaseColumn.class );
            constructorParams[i] = type;
            constructorParamGenerics[i] = ( (ParameterizedType) type.getGenericSuperclass() ).getActualTypeArguments()[0].getClass();
        }
        //primary key required
        if ( primaryKey == -1 ) return null;
        Constructor<T> constructor = clazz.getConstructor( constructorParams );
        Object[] params = new Object[components.length];
        //build primary key before creating properties
        PrimaryKey<?> primKey = ModelProperties.create( constructorParamGenerics[primaryKey], columnarData[primaryKey] ).buildPrimaryKey();
        params[primaryKey] = primKey;
        for ( int i = 0; i < components.length; i++ )
        {
            //build all properties - skip the primary key
            if ( i == primaryKey ) continue;
            ModelProperties<?> properties = ModelProperties.create( constructorParamGenerics[i], columnarData[i] );
            params[i] = foreignKeys[i] != null ? properties.buildForeignKey( model.value(), primKey, foreignKeys[i].table(), foreignKeys[i].keyName() ) : properties.build( model.value(), primKey );
        }
        return constructor.newInstance( params );
    }

}
