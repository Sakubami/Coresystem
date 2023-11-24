package net.haraxx.coresystem.api.command;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Represents a command for handling textual input
 *
 * @author Juyas
 * @version 24.11.2023
 * @see CommandGroup
 * @see BukkitCommand
 * @see ArgumentRange
 * @see TabCompletion
 * @since 12.11.2023
 */
public interface ICommand
{

    /**
     * The name of the command.
     *
     * @return the name of the command
     */
    String name();

    /**
     * Potential substitutions for the name of the command
     *
     * @return alternative names
     */
    String[] aliases();

    /**
     * An optional permission for the command. May be null, if there is no permission to check.
     *
     * @return the permission for this command, or <code>null</code> if there is none
     */
    @Nullable
    String permission();

    /**
     * The signature of the command should contain all arguments with short but descriptive names.
     * It is primarily used for the help page when displaying the command signature. <br>
     * e.g.: <code>/cmd subcmd [arg1] (option)</code> <br>
     * In this example the returning list should be like ["arg1","option"] <b>without</b> the brackets.
     *
     * @return the commands signature
     */
    List<String> signature();

    /**
     * The range of valid arguments for this command.
     *
     * @return the {@link ArgumentRange} of this command
     */
    ArgumentRange range();

    /**
     * The textual description of this command
     *
     * @return the description of this command
     */
    List<String> description();

    /**
     * Called by another class. Expects a {@link CommandSender} and an array with command arguments.
     * If this gets called as a subcommand by a {@link CommandGroup} the arguments get shifted to ensure they always start at index 0 (zero).
     *
     * @param sender the sender of the command
     * @param args   the arguments for this command
     */
    void onCommand( CommandSender sender, String[] args );

    /**
     * Called by another class. Expects the same parameters as {@link #onCommand(CommandSender, String[])},
     * but instead of executing the command just proposes all possible options for the last argument.
     * By default, this method calls {@link #tabOptions(CommandSender, String[])} by stripping the rawArgs, since they aren't used as much.
     *
     * @param sender  the sender of the command
     * @param args    the arguments for this command
     * @param rawArgs all arguments for the root command
     *
     * @return all completion options for the last argument
     */
    default TabCompletion tabOptions( CommandSender sender, String[] args, String[] rawArgs )
    {
        return tabOptions( sender, args );
    }

    /**
     * Mostly identical to {@link #tabOptions(CommandSender, String[], String[])}.
     * Should get called by the other method instead of another class.
     *
     * @param sender the sender of the command
     * @param args   the arguments for this command
     *
     * @return all completion options for the last argument
     */
    default TabCompletion tabOptions( CommandSender sender, String[] args )
    {
        return TabCompletion.NONE;
    }

}
