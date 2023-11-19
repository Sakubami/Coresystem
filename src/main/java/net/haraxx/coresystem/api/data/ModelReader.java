package net.haraxx.coresystem.api.data;

import net.haraxx.coresystem.api.data.impl.*;
import net.haraxx.coresystem.api.data.model.*;

import java.lang.reflect.*;
import java.util.HashMap;

/**
 * @author Juyas
 * @version 16.11.2023
 * @since 16.11.2023
 */
public final class ModelReader
{

    public static <T> T buildModel( Class<T> clazz ) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException
    {
        //TODO more solid argument checking, exceptions and control flow handling
        if ( !clazz.isRecord() || !clazz.isAnnotationPresent( Model.class ) ) return null;
        Model model = clazz.getDeclaredAnnotation( Model.class );
        RecordComponent[] components = clazz.getRecordComponents();
        Class<?>[] constructorParams = new Class[components.length];
        Class<?>[] constructorParamGenerics = new Class[components.length];
        ColumnSettings[] columnarData = new ColumnSettings[components.length];
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
            if ( !component.isAnnotationPresent( DatabaseColumn.class ) ) return null;
            columnarData[i] = ColumnSettings.of( component.getDeclaredAnnotation( DatabaseColumn.class ) );
            constructorParams[i] = type;
            constructorParamGenerics[i] = ( (ParameterizedType) type.getGenericSuperclass() ).getActualTypeArguments()[0].getClass();
        }
        //primary key required
        if ( primaryKey == -1 ) return null;
        Constructor<T> constructor = clazz.getConstructor( constructorParams );
        HashMap<String, ModelProperty<?>> properties = new HashMap<>();
        Object[] params = new Object[components.length];
        //build primary key before creating properties
        PrimaryKey primKey = new PrimaryKeyImpl( columnarData[primaryKey] );
        params[primaryKey] = primKey;
        for ( int i = 0; i < components.length; i++ )
        {
            //build all properties - skip the primary key
            if ( i == primaryKey ) continue;
            ColumnImpl<?> column = ColumnImpl.of( constructorParamGenerics[i], columnarData[i], model.value(), primKey );
            properties.put( columnarData[i].columnName(), column );
            params[i] = column;
        }
        T generatedModel = constructor.newInstance( params );
        if ( ModelBase.class.isAssignableFrom( clazz ) )
        {
            Field modelField = ModelBase.class.getDeclaredField( "model" );
            modelField.setAccessible( true );
            modelField.set( generatedModel, new DataModel( model.value(), primKey, properties ) );
        }
        return generatedModel;
    }

}
