package net.haraxx.coresystem.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandRunner {
    void runCommand(CommandSender exe, String[] args);
    List<String> tabComplete(String[] args);
}
