package net.haraxx.coresystem.commands.old;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandRunner {
    void runCommand(CommandSender exe, String[] args);
    List<String> tabComplete(CommandSender exe, String[] args);
}
