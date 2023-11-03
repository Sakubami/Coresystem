package net.haraxx.coresystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    private final HashMap<String, CommandRunner> commands = new HashMap<>();

    public void registerCoreSubCommand(String s, CommandRunner runner) {
        commands.put(s,runner);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("core")) {
            if (args.length != 0) {
                commands.get(args[0]).runCommand(sender, args);
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (cmd.getName().equalsIgnoreCase("core")) {
            if (args.length != 0) {
                return commands.get(args[0]).tabComplete(args);
            }
        }
        return Collections.emptyList();
    }
}
