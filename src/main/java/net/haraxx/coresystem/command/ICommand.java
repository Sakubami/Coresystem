package net.haraxx.coresystem.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Juyas
 * @version 12.11.2023
 * @since 12.11.2023
 */
public interface ICommand
{

    String name();

    String[] aliases();

    String permission();

    List<String> signature();

    ArgumentRange range();

    List<String> description();

    void onCommand( CommandSender sender, String[] args );

    List<String> tabOptions( CommandSender sender, String[] args );

}
