package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.*;

import java.lang.reflect.*;
import java.util.HashMap;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
final class ModelReader
{

    static <T extends ModelBase> T buildModel( Class<T> clazz ) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException
    {
        //TODO more solid argument checking, exceptions and control flow handling
        if ( !clazz.isRecord() || !clazz.isAnnotationPresent( Model.class ) ) return null;
        Model model = clazz.getDeclaredAnnotation( Model.class );
        //prepare data reading
        Field[] fields = clazz.getDeclaredFields();
        Class<?>[] constructorParamGenerics = new Class[fields.length];
        ColumnSettings[] columnarData = new ColumnSettings[fields.length];
        int primaryKey = -1;
        //read and check field data
        for ( int i = 0; i < fields.length; i++ )
        {
            Field field = fields[i];
            //check for model property types - all of them require the annotation
            Class<?> type = field.getType();
            //only modelproperties allowed
            if ( !ModelProperty.class.isAssignableFrom( type ) ) return null;
            //note down the primary key
            if ( PrimaryKey.class.isAssignableFrom( type ) )
            {
                //only one primary key valid
                if ( primaryKey != -1 ) return null;
                primaryKey = i;
            }
            //only primary keys and columns
            else if ( !Column.class.isAssignableFrom( type ) ) return null;
            //column description is mandatory
            if ( !field.isAnnotationPresent( DatabaseColumn.class ) ) return null;
            columnarData[i] = ColumnSettings.of( field.getDeclaredAnnotation( DatabaseColumn.class ) );
            constructorParamGenerics[i] = ( (ParameterizedType) type.getGenericSuperclass() ).getActualTypeArguments()[0].getClass();
        }
        //primary key required
        if ( primaryKey == -1 ) return null;
        Constructor<T> constructor = clazz.getConstructor();
        constructor.setAccessible( true );
        T generatedModel = constructor.newInstance();
        HashMap<String, Column<?>> properties = new HashMap<>();
        //build primary key and place it into the generated model
        PrimaryKey primKey = new PrimaryKeyImpl( columnarData[primaryKey] );
        fields[primaryKey].set( generatedModel, primKey );
        for ( int i = 0; i < fields.length; i++ )
        {
            //build all properties - skip the primary key
            if ( i == primaryKey ) continue;
            DataColumn<?> column = DataColumn.of( constructorParamGenerics[i], columnarData[i], model, primKey );
            properties.put( columnarData[i].columnName(), column );
        }
        Field modelField = ModelBase.class.getDeclaredField( "model" );
        modelField.setAccessible( true );
        modelField.set( generatedModel, new DataModel( model.schema(), model.table(), primKey, properties ) );
        return generatedModel;
    }

}
