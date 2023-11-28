package net.haraxx.coresystem.api.command;

import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.util.*;

/**
 * @author Juyas
 * @version 12.11.2023
 * @since 12.11.2023
 */
public abstract class BukkitCommand implements ICommand, CommandExecutor, TabCompleter
{

    private static final String MESSAGE_MISSING_PERMISSIONS = "§cKeine Berechtigung.";
    private static final String MESSAGE_INVALID_ARGUMENTS = "§cFalsche Parameter.";

    private final String name;
    private final String[] aliases;
    private String permission;
    private List<String> description;
    private List<String> signature;
    private ArgumentRange range;

    public BukkitCommand( String name, String... aliases )
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

    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String s, String[] args )
    {
        if ( !range().inRange( args.length ) )
        {
            sender.sendMessage( MESSAGE_INVALID_ARGUMENTS );
            return true;
        }
        onCommand( sender, args );
        return true;
    }

    @Override
    public List<String> onTabComplete( CommandSender sender, Command cmd, String s, String[] args )
    {
        TabCompletion tabCompletion = tabOptions( sender, args, args );
        return tabCompletion.tabOptions().stream()
                .distinct()
                .filter( option -> option.toLowerCase().startsWith( args[args.length - 1].toLowerCase() ) )
                .sorted()
                .toList();
    }

    public final boolean register()
    {
        PluginCommand pluginCommand = Bukkit.getPluginCommand( name );
        if ( pluginCommand == null ) return false;
        pluginCommand.setAliases( Arrays.asList( aliases() ) );
        pluginCommand.setPermission( permission() );
        pluginCommand.setDescription( String.join( "\n", description() ) );
        pluginCommand.setPermissionMessage( MESSAGE_MISSING_PERMISSIONS );
        StringBuilder usage = new StringBuilder();
        usage.append( "/" ).append( name() );
        for ( int s = 0; s < signature().size(); s++ )
        {
            usage.append( " " );
            String arg = signature().get( s );
            if ( range().inRange( s ) ) usage.append( '(' ).append( arg ).append( ')' );
            else usage.append( '[' ).append( arg ).append( ']' );
        }
        pluginCommand.setUsage( usage.toString() );
        pluginCommand.setExecutor( this );
        pluginCommand.setTabCompleter( this );
        return true;
    }

}
