package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.*;

import java.lang.reflect.*;
import java.util.*;

import static net.haraxx.coresystem.api.data.base.ModelConstraintsDissatisfiedException.ModelConstraint.*;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 16.11.2023
 */
final class ModelReader
{

    static <T extends ModelBase> T buildModel( Class<T> clazz ) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, ModelConstraintsDissatisfiedException
    {
        //the model annotation is required for any model to be loaded, since it contains crucial data
        if ( !Objects.requireNonNull( clazz ).isAnnotationPresent( Model.class ) )
            throw new ModelConstraintsDissatisfiedException( MODEL_ANNOTATION, clazz.getSimpleName(), null );
        Model model = clazz.getDeclaredAnnotation( Model.class );
        if ( model.schema().isBlank() )
            throw new ModelConstraintsDissatisfiedException( MODEL_DESCRIPTORS, clazz.getSimpleName(), "model schema is empty" );
        if ( model.table().isBlank() )
            throw new ModelConstraintsDissatisfiedException( MODEL_DESCRIPTORS, clazz.getSimpleName(), "model (table)name is empty" );
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
            field.setAccessible( true );
            Class<?> propertyType = field.getType();
            //detect the PrimaryKey
            if ( PrimaryKey.class.isAssignableFrom( propertyType ) )
            {
                //if there is already a primary - multiple primary keys are not allowed
                if ( primaryKey != -1 )
                    throw new ModelConstraintsDissatisfiedException( SINGLE_PRIMARY_KEY, model.schema() + "." + model.table(),
                            "two fields of type \"PrimaryKey\" found" );
                primaryKey = i;
            }
            //if it's not a PrimaryKey, it is required to be a Column
            else if ( !Column.class.isAssignableFrom( propertyType ) )
                throw new ModelConstraintsDissatisfiedException( MODEL_PROPERTY_TYPE, model.schema() + "." + model.table(),
                        "field \"" + field.getName() + "\" is of type \"" + propertyType.getSimpleName() + "\"" );
            //DatabaseColumn annotation is required for all properties - including the PrimaryKey
            if ( !field.isAnnotationPresent( DatabaseColumn.class ) )
                throw new ModelConstraintsDissatisfiedException( COLUMN_ANNOTATION, model.schema() + "." + model.table(),
                        "caused by field \"" + field.getName() + "\" of type \"" + propertyType.getSimpleName() + "\"" );
            //if all conditions are checked, read the data
            ColumnSettings settings = ColumnSettings.of( field.getDeclaredAnnotation( DatabaseColumn.class ) );
            if ( settings.sqlType().isBlank() || settings.columnName().isBlank() )
                throw new ModelConstraintsDissatisfiedException( COLUMN_DESCRIPTORS, model.schema() + "." + model.table(),
                        "caused by field \"" + field.getName() + "\" of type \"" + propertyType.getSimpleName() + "\"" );
            columnarData[i] = settings;
            if ( primaryKey == i )
                propertyGenerics[i] = Long.class;
            else
                propertyGenerics[i] = (Class<?>) ( (ParameterizedType) field.getGenericType() ).getActualTypeArguments()[0];

        }
        //if there is no PrimaryKey, it's an invalid model
        if ( primaryKey == -1 )
            throw new ModelConstraintsDissatisfiedException( SINGLE_PRIMARY_KEY, model.schema() + "." + model.table(),
                    "no field of type \"PrimaryKey\" found" );
        //retrieve the empty class constructor - it will get the compiler default constructor if there is a defined one
        Constructor<T> constructor = clazz.getConstructor();
        Objects.requireNonNull( constructor, "No empty constructor for class " + clazz.getSimpleName() );
        constructor.setAccessible( true );
        //construct a new instance of the model
        T generatedModel = constructor.newInstance();

        // ---------- BUILD AND APPLY THE DATAMODEL ----------
        List<Column<?>> properties = new ArrayList<>( fields.length - 1 );
        //build PrimaryKey first and place it into the generated model
        PrimaryKey primKey = new PrimaryKeyImpl( columnarData[primaryKey] );
        fields[primaryKey].set( generatedModel, primKey );

        //build all properties - skip the primary key
        for ( int i = 0; i < fields.length; i++ )
        {
            if ( i == primaryKey ) continue;
            DataColumn<?> column = DataColumn.of( propertyGenerics[i], columnarData[i], model, primKey );
            fields[i].set( generatedModel, column );
            properties.add( column );
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
