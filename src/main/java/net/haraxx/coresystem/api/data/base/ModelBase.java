package net.haraxx.coresystem.api.data.base;

import net.haraxx.coresystem.api.data.model.Column;
import net.haraxx.coresystem.api.data.query.DatabaseHandler;
import net.haraxx.coresystem.api.util.Try;

import java.util.logging.Level;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 19.11.2023
 */
public abstract class ModelBase
{

    private DataModel model = null;

    public final DataModel getModel()
    {
        return model;
    }

    public final boolean loadKey( String field )
    {
        Column<?> property = model.columns().get( field );
        if ( property == null ) return false;
        if ( !property.value().isCached() ) return false;
        DatabaseHandler.getInstance().loadKeyByField( model.primaryKey(), model.modelSchema(), model.modelName(), property );
        return true;
    }

    public final boolean load()
    {
        if ( model.primaryKey().value().get().isEmpty() ) return false;
        DatabaseHandler.getInstance().loadFieldsByKey( model );
        return true;
    }

    public final boolean delete()
    {
        if ( model.primaryKey().value().get().isEmpty() ) return false;
        DatabaseHandler.getInstance().deleteEntry( model );
        return true;
    }

    public final boolean insert()
    {
        for ( Column<?> column : model.columns().values() )
        {
            //non-null values should always be non-null before saving
            if ( column.settings().nonNull() && !column.value().isCached() ) return false;
        }
        DatabaseHandler.getInstance().insertModelAndUpdateKey( model );
        return true;
    }

    public static <T extends ModelBase> T generate( Class<T> modelClass )
    {
        return Try.log( () -> ModelReader.buildModel( modelClass ), Level.SEVERE, "Could not build model for class \"" + modelClass.getCanonicalName() + "\"" );
    }

}
