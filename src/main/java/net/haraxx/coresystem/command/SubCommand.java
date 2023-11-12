package net.haraxx.coresystem.command;

import java.util.*;

/**
 * @author Juyas
 * @version 12.11.2023
 * @since 12.11.2023
 */
public abstract class SubCommand implements ICommand
{

    private final String name;
    private final String[] aliases;
    private String permission;
    private List<String> description;
    private List<String> signature;
    private ArgumentRange range;

    public SubCommand( String name, String... aliases )
    {
        this.name = name;
        this.aliases = aliases;
        //default: no range, empty signature, empty description, no permission
        this.permission = null;
        this.range = ArgumentRange.all();
        this.signature = Collections.emptyList();
        this.description = Collections.emptyList();
    }

    protected void setDescription( List<String> description )
    {
        this.description = description;
    }

    protected void setDescription( String... description )
    {
        this.description = Arrays.asList( description );
    }

    protected void setPermission( String permission )
    {
        this.permission = permission;
    }

    protected void setMinArgs( int minArgs )
    {
        this.range = ArgumentRange.of( minArgs, this.range.getMax() );
    }

    protected void setArgumentRange( ArgumentRange range )
    {
        this.range = range;
    }

    protected void setSignature( String... argumentSignature )
    {
        this.signature = Arrays.asList( argumentSignature );
        this.range = ArgumentRange.of( this.range.getMin(), argumentSignature.length );
    }

    @Override
    public String permission()
    {
        return permission;
    }

    @Override
    public List<String> description()
    {
        return description;
    }

    @Override
    public List<String> signature()
    {
        return signature;
    }

    @Override
    public ArgumentRange range()
    {
        return range;
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String[] aliases()
    {
        return aliases;
    }

}
