package net.haraxx.coresystem.commands.item;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("NullableProblems")
public interface ModuleCommandExecutor extends CommandExecutor {

    @Override
    default boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {

            runCommand(sender, cmd, label, args);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    void runCommand(CommandSender sender, Command cmd, String label, String[] args);

}
