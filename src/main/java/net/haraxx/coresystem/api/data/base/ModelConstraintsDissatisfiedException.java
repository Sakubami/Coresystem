package net.haraxx.coresystem.api.data.base;

/**
 * @author Juyas
 * @version 22.11.2023
 * @since 22.11.2023
 */
public class ModelConstraintsDissatisfiedException extends Exception
{

    public enum ModelConstraint
    {
        UNKNOWN,
        MODEL_ANNOTATION,
        MODEL_DESCRIPTORS,
        COLUMN_ANNOTATION,
        SINGLE_PRIMARY_KEY,
        MODEL_PROPERTY_TYPE,
        COLUMN_DESCRIPTORS
    }

    public ModelConstraintsDissatisfiedException( ModelConstraint constraint, String model, String info )
    {
        super( "Constraint " + ( constraint == null ? ModelConstraint.UNKNOWN.name() : constraint.name() )
                + " not satisfied in model \"" + model + "\"" + ( info != null && !info.isEmpty() ? ". Additional info: " + info : "" ) );
    }

}
