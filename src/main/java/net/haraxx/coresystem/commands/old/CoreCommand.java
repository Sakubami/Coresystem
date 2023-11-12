package net.haraxx.coresystem.commands.old;

import net.haraxx.coresystem.builder.Chat;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CoreCommand implements CommandExecutor, TabCompleter {

    private final HashMap<String, CommandRunner> commands = new HashMap<>();

    public void registerCoreSubCommand(String s, CommandRunner runner) {
        commands.put(s.toLowerCase(), runner);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("haraxx")) {
            if (args.length != 0) {
                if (commands.get(args[0]) != null) {
                    commands.get(args[0]).runCommand(sender, args);
                } else {
                    sender.sendMessage(Chat.format("Dieser §6SubCommand §7existiert nicht"));
                }
            } else {
                sender.sendMessage(Chat.format("Bitte §6Subcommand §7angeben"));
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("haraxx")) {
            if (args.length > 1) {
                CommandRunner commandRunner = commands.get(args[0].toLowerCase());
                if (commandRunner != null)
                    return commandRunner.tabComplete(sender, args);
            } else if (args.length == 1) {
                return commands.keySet().stream().filter(str -> str.startsWith(args[0].toLowerCase())).toList();
            }
        }
        return Collections.emptyList();
    }
}
