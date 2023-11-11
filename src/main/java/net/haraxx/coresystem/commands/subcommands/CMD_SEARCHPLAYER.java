package net.haraxx.coresystem.commands.subcommands;

import net.haraxx.coresystem.commands.CommandRunner;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CMD_SEARCHPLAYER implements CommandRunner {
    @Override
    public void runCommand(CommandSender exe, String[] args) { }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
