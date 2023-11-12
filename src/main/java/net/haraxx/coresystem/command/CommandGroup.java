package net.haraxx.coresystem.command;

import net.haraxx.coresystem.message.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.awt.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.*;

/**
 * @author Juyas
 * @version 12.11.2023
 * @since 12.11.2023
 */
public final class CommandGroup implements ICommand
{

    private static final String MESSAGE_MISSING_PERMISSIONS = "§cKeine Berechtigung.";
    private static final String MESSAGE_INVALID_ARGUMENTS = "§cFalsche Parameter.";
    private static final String MESSAGE_UNKNOWN_COMMAND = "§cBefehl \"{0}\" nicht gefunden.";
    private static final String HELP_CLICK_TO_SUGGEST = "Links-Klick zum Einfügen in den Chat";
    private static final String HELP_HEADER_START = "Hilfe für";
    private static final String HELP_PAGE = "Seite";
    private static final String HELP_NEXT_PAGE = "Vor zu Seite {0}";
    private static final String HELP_PREV_PAGE = "Zurück zu Seite {0}";

    private static final String HELP_KEYWORD = "help";
    private static final int ITEMS_PER_HELP_PAGE = 4;

    private final String name;
    private final List<ICommand> subCommands;
    private final Set<String> commandNames;
    private String[] aliases;
    private String permission;
    private List<String> description;
    private String parentSignature;

    public CommandGroup( String name )
    {
        this.name = name;
        this.aliases = new String[0];
        this.description = Collections.emptyList();
        this.subCommands = new ArrayList<>();
        this.commandNames = new HashSet<>();
        this.parentSignature = "";
    }

    public CommandGroup( PluginCommand command )
    {
        Objects.requireNonNull( command, "the given plugin-command is null" );
        this.name = command.getName();
        this.aliases = command.getAliases().toArray( new String[0] );
        this.description = new ArrayList<>();
        this.description.add( command.getDescription() );
        this.permission = command.getPermission();
        this.subCommands = new ArrayList<>();
        this.commandNames = new HashSet<>();
        this.parentSignature = "";
    }

    public CommandGroup setAliases( String... aliases )
    {
        this.aliases = aliases;
        return this;
    }

    public CommandGroup setPermission( String permission )
    {
        this.permission = permission;
        return this;
    }

    public CommandGroup setDescription( String... description )
    {
        this.description = List.of( description );
        return this;
    }

    public CommandGroup addSubCommand( ICommand subCommand )
    {
        if ( subCommand instanceof CommandGroup )
            ( (CommandGroup) subCommand ).setParentSignature( this.parentSignature + " " + name );
        this.subCommands.add( subCommand );
        this.commandNames.add( subCommand.name() );
        this.commandNames.addAll( Arrays.asList( subCommand.aliases() ) );
        return this;
    }

    public boolean register()
    {
        PluginCommand pluginCommand = Bukkit.getPluginCommand( name );
        if ( pluginCommand == null ) return false;
        pluginCommand.setExecutor( ( s, c, l, a ) -> {
            this.onCommand( s, a );
            return true;
        } );
        pluginCommand.setTabCompleter( ( s, c, l, a ) -> filterAndSort( a[a.length - 1], this.tabOptions( s, a ) ) );
        return true;
    }

    private List<String> filterAndSort( String arg, List<String> options )
    {
        String argument = arg.toLowerCase();
        return options.stream().filter( option -> option.toLowerCase().startsWith( argument ) ).sorted().toList();
    }

    private void setParentSignature( String parentSignature )
    {
        this.parentSignature = parentSignature;
    }

    public List<ICommand> subCommands()
    {
        return subCommands;
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
    public String permission()
    {
        return permission;
    }

    @Override
    public List<String> signature()
    {
        return List.of( "subcommands" );
    }

    @Override
    public ArgumentRange range()
    {
        return ArgumentRange.fix( 1 );
    }

    @Override
    public List<String> description()
    {
        return description;
    }

    private String[] cutArgs( String[] args )
    {
        if ( args.length == 0 ) return new String[0];
        if ( args.length == 1 ) return new String[0];
        String[] cutArgs = new String[args.length - 1];
        System.arraycopy( args, 0, cutArgs, 1, args.length - 1 );
        return cutArgs;
    }

    private MessageBuilder.Messages buildHeader( int page, boolean min, boolean max )
    {
        MessageBuilder msg = MessageBuilder.create();
        TextComponent headerSpacing = new TextComponent( "==========" );
        headerSpacing.setColor( ChatColor.GRAY );
        msg.add( headerSpacing.duplicate() )
                .beginElement( HELP_HEADER_START )
                .color( ChatColor.DARK_AQUA )
                .endElement()
                .raw( " " )
                .beginElement( name )
                .color( ChatColor.GOLD )
                .bold()
                .endElement()
                .legacy( " §r§7| " );
        if ( min )
            msg.beginElement( "<<" ).color( ChatColor.DARK_GRAY ).endElement();
        else msg.beginElement( "<<" )
                .color( ChatColor.YELLOW )
                .hover( HoverEvent.Action.SHOW_TEXT, new Text( TextComponent.fromLegacyText( MessageFormat.format( HELP_PREV_PAGE, ( page - 1 ) ), ChatColor.GRAY ) ) )
                .click( ClickEvent.Action.RUN_COMMAND, "/" + ( parentSignature.isEmpty() ? "" : parentSignature + " " ) + name + " " + HELP_KEYWORD + " " + ( page - 1 ) )
                .endElement();
        msg.raw( " " )
                .beginElement( HELP_PAGE + " " + page )
                .color( Color.YELLOW )
                .endElement()
                .raw( " " );
        if ( max )
            msg.beginElement( ">>" ).color( ChatColor.DARK_GRAY ).endElement();
        else msg.beginElement( ">>" )
                .color( ChatColor.YELLOW )
                .hover( HoverEvent.Action.SHOW_TEXT, new Text( TextComponent.fromLegacyText( MessageFormat.format( HELP_NEXT_PAGE, ( page - 1 ) ), ChatColor.GRAY ) ) )
                .click( ClickEvent.Action.RUN_COMMAND, "/" + ( parentSignature.isEmpty() ? "" : parentSignature + " " ) + name + " " + HELP_KEYWORD + " " + ( page + 1 ) )
                .endElement();
        msg.raw( " " )
                .add( headerSpacing );
        return msg.build();
    }

    public void displayHelp( CommandSender sender, int page )
    {
        //find start and end safely
        int pages = ( subCommands.size() / ITEMS_PER_HELP_PAGE ) + ( subCommands.size() % ITEMS_PER_HELP_PAGE != 0 ? 1 : 0 );
        page = Math.max( 1, Math.min( page, pages ) );
        int last = ITEMS_PER_HELP_PAGE * page;
        int first = last - ITEMS_PER_HELP_PAGE;
        last = Math.max( last, subCommands.size() );
        buildHeader( page, page == 1, page == pages ).send( sender );
        for ( int i = first; i < last; i++ )
        {
            ICommand cmd = subCommands.get( i );
            StringBuilder builder = new StringBuilder();
            builder.append( cmd.name() );
            for ( int s = 0; s < cmd.signature().size(); s++ )
            {
                builder.append( " " );
                String arg = cmd.signature().get( s );
                if ( cmd.range().inRange( s ) ) builder.append( '(' ).append( arg ).append( ')' );
                else builder.append( '[' ).append( arg ).append( ']' );
            }
            String line = "/" + parentSignature + " " + builder;
            MessageBuilder.create()
                    .beginElement( line )
                    .color( ChatColor.BLUE )
                    .hover( HoverEvent.Action.SHOW_TEXT, new Text( TextComponent.fromLegacyText( HELP_CLICK_TO_SUGGEST, ChatColor.GRAY ) ) )
                    .click( ClickEvent.Action.SUGGEST_COMMAND, line )
                    .endElement()
                    .build()
                    .send( sender );
            cmd.description().forEach( desc -> sender.sendMessage( ChatColor.GRAY + desc ) );
        }
    }

    private ICommand getCommand( String cmd )
    {
        return subCommands.stream().filter( c -> c.name().equalsIgnoreCase( cmd )
                        || Arrays.stream( c.aliases() ).anyMatch( alias -> alias.equalsIgnoreCase( cmd ) ) )
                .findFirst().orElse( null );
    }

    @Override
    public void onCommand( CommandSender sender, String[] args )
    {
        if ( permission != null && !sender.hasPermission( permission ) )
        {
            sender.sendMessage( MESSAGE_MISSING_PERMISSIONS );
            return;
        }
        if ( args.length == 0 ) displayHelp( sender, 1 );
        if ( args[0].equalsIgnoreCase( HELP_KEYWORD ) )
        {
            int page = 1;
            if ( args.length == 2 )
            {
                try
                {
                    page = Integer.parseInt( args[1] );
                }
                catch ( Exception ignored )
                {
                }
            }
            displayHelp( sender, page );
            return;
        }
        ICommand command = getCommand( args[0] );
        if ( command == null )
        {
            sender.sendMessage( MessageFormat.format( MESSAGE_UNKNOWN_COMMAND, args[0] ) );
            return;
        }
        if ( command instanceof CommandGroup )
        {
            //command groups get pushed on and handle themselves
            command.onCommand( sender, cutArgs( args ) );
        }
        else
        {
            //check for argument amount
            if ( !command.range().inRange( args.length - 1 ) )
            {
                sender.sendMessage( MESSAGE_INVALID_ARGUMENTS );
            }
            //check for permissions if there are some
            else if ( command.permission() != null && !sender.hasPermission( command.permission() ) )
            {
                sender.sendMessage( MESSAGE_MISSING_PERMISSIONS );
            }
            else
            {
                //execute the command regularly
                command.onCommand( sender, cutArgs( args ) );
            }
        }
    }

    @Override
    public List<String> tabOptions( CommandSender sender, String[] args )
    {
        if ( args.length == 1 )
            return new ArrayList<>( commandNames );
        else if ( args.length > 1 )
        {
            ICommand command = getCommand( args[0] );
            if ( command != null ) return command.tabOptions( sender, cutArgs( args ) );
        }
        return Collections.emptyList();
    }

}
