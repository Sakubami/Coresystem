package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.*;
import net.haraxx.coresystem.api.data.query.DatabaseHandler;
import net.haraxx.coresystem.api.util.Try;

import java.util.logging.Level;

/**
 * The base for every model providing some functionality
 *
 * @author Juyas
 * @version 22.11.2023
 * @see Model
 * @see PrimaryKey
 * @see Column
 * @since 19.11.2023
 */
public abstract class ModelBase
{

    private DataModel model = null;

    /**
     * Gets the {@link DataModel} representing the subclass and containing all model property members
     *
     * @return the {@link DataModel}
     */
    public final DataModel getModel()
    {
        return model;
    }

    /**
     * Attempts to load the primary key of this model by using another field (preferable a unique one).
     *
     * @param field the field, which has to be set beforehand, to identify the database entry
     *
     * @return false, if the specified field doesn't exist or isn't set, true otherwise
     */
    public final boolean loadKey( String field )
    {
        Column<?> property = model.getColumn( field );
        if ( property == null ) return false;
        if ( !property.value().isCached() ) return false;
        DatabaseHandler.getInstance().loadKeyByField( model.primaryKey(), model.modelSchema(), model.modelName(), property );
        return true;
    }

    /**
     * Attempts to load the entire model instance using the primary key from the database backend.
     *
     * @return false, if the primary key has not been set, true otherwise
     */
    public final boolean load()
    {
        if ( model.primaryKey().value().get().isEmpty() ) return false;
        DatabaseHandler.getInstance().loadFieldsByKey( model );
        return true;
    }

    /**
     * Attempts to delete the entire model instance using the primary key from the database backend.
     *
     * @return false, if the primary key has not been set, true otherwise
     */
    public final boolean delete()
    {
        if ( model.primaryKey().value().get().isEmpty() ) return false;
        DatabaseHandler.getInstance().deleteEntry( model );
        return true;
    }

    /**
     * Attempts to insert the entire model instance into the database backend as a new entry. <br>
     * Retrieves the new primary key value afterward.
     *
     * @return false, if any column specified as non-null is null, true otherwise
     */
    public final boolean insert()
    {
        for ( Column<?> column : model.columns() )
        {
            //non-null values should always be non-null before saving
            if ( column.settings().nonNull() && !column.value().isCached() ) return false;
        }
        DatabaseHandler.getInstance().insertModelAndUpdateKey( model );
        return true;
    }

    /**
     * Generates a new model instance by the given class using the {@link ModelReader#buildModel(Class)} method
     *
     * @param modelClass the model class satisfying all constraints
     * @param <T>        used to constraint the model class to be a subclass of {@link ModelBase}
     *
     * @return a new instance of the given class with generated values and ready to use
     */
    public static <T extends ModelBase> T generate( Class<T> modelClass )
    {
        return Try.log( () -> ModelReader.buildModel( modelClass ), Level.SEVERE, "Could not build model for class \"" + modelClass.getCanonicalName() + "\"" );
    }

}
