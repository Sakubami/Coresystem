package net.haraxx.coresystem.commands;

import net.haraxx.coresystem.builder.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Commands implements CommandExecutor, TabCompleter {

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
                    return commandRunner.tabComplete(args);
            } else if (args.length == 1) {
                return commands.keySet().stream().filter(str -> str.startsWith(args[0].toLowerCase())).toList();
            }
        }
        return Collections.emptyList();
    }
}
