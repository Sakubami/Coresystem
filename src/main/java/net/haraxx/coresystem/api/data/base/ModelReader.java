package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.*;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
final class ModelReader
{

    static <T extends ModelBase> T buildModel( Class<T> clazz ) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException
    {
        //the model annotation is required for any model to be loaded, since it contains crucial data
        if ( !Objects.requireNonNull( clazz ).isAnnotationPresent( Model.class ) ) return null;
        Model model = clazz.getDeclaredAnnotation( Model.class );
        //all declared fields are considered model properties
        Field[] fields = clazz.getDeclaredFields();
        //space to save property information
        Class<?>[] propertyGenerics = new Class[fields.length];
        ColumnSettings[] columnarData = new ColumnSettings[fields.length];
        //a single primary key is allowed and a single key is required simultaneously
        int primaryKey = -1;
        //resolve all fields and validate them
        for ( int i = 0; i < fields.length; i++ )
        {
            Field field = fields[i];
            Class<?> propertyType = field.getType();
            //all model properties need to be explicitly implementing ModelProperty
            if ( !ModelProperty.class.isAssignableFrom( propertyType ) ) return null;
            //detect the PrimaryKey
            if ( PrimaryKey.class.isAssignableFrom( propertyType ) )
            {
                //if there is already a primary - multiple primary keys are not allowed
                if ( primaryKey != -1 ) return null;
                primaryKey = i;
            }
            //if it's not a PrimaryKey, it is required to be a Column
            else if ( !Column.class.isAssignableFrom( propertyType ) ) return null;
            //DatabaseColumn annotation is required for all properties - including the PrimaryKey
            if ( !field.isAnnotationPresent( DatabaseColumn.class ) ) return null;
            //if all conditions are checked, read the data
            columnarData[i] = ColumnSettings.of( field.getDeclaredAnnotation( DatabaseColumn.class ) );
            propertyGenerics[i] = ( (ParameterizedType) propertyType.getGenericSuperclass() ).getActualTypeArguments()[0].getClass();
        }
        //if there is no PrimaryKey, it's an invalid model
        if ( primaryKey == -1 ) return null;
        //retrieve the empty class constructor - it will get the compiler default constructor if there is a defined one
        Constructor<T> constructor = clazz.getConstructor();
        constructor.setAccessible( true );
        //construct a new instance of the model
        T generatedModel = constructor.newInstance();

        // ---------- BUILD AND APPLY THE DATAMODEL ----------
        HashMap<String, Column<?>> properties = new HashMap<>();
        //build PrimaryKey first and place it into the generated model
        PrimaryKey primKey = new PrimaryKeyImpl( columnarData[primaryKey] );
        fields[primaryKey].set( generatedModel, primKey );

        //build all properties - skip the primary key
        for ( int i = 0; i < fields.length; i++ )
        {
            if ( i == primaryKey ) continue;
            DataColumn<?> column = DataColumn.of( propertyGenerics[i], columnarData[i], model, primKey );
            properties.put( columnarData[i].columnName(), column );
        }
        //create model
        DataModel dataModel = new DataModel( model.schema(), model.table(), primKey, properties );
        //directly write model into the generated class
        Field modelField = ModelBase.class.getDeclaredField( "model" );
        modelField.setAccessible( true );
        modelField.set( generatedModel, dataModel );
        return generatedModel;
    }

}
