package net.haraxx.coresystem.commands.subcommands;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.commands.CommandRunner;
import net.haraxx.coresystem.commands.Utils;
import net.haraxx.coresystem.plugins.spawning.WorldSpawnConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Spawn implements CommandRunner {

    private final List<String> actions = List.of("setfirstspawn", "setwoldspawn");

    @Override
    public void runCommand(CommandSender exe, String[] args) {
        Player p = (Player) exe;
        if (Utils.getDefaultPerms(p)) {
            if (args.length == 2) {
                switch (args[1]) {
                    case "setwoldspawn" -> {
                        WorldSpawnConfig.get().setWorldSpawnLocation(p.getLocation());
                        p.sendMessage(Chat.format("set spawnlocation to §6" + p.getLocation().getBlockX() + "§7.§6" + p.getLocation().getBlockY() + "§7.§6" + p.getLocation().getBlockZ()));
                    }
                    case "setfirstspawn" -> {
                        WorldSpawnConfig.get().setFirstLocation(p.getLocation());
                        p.sendMessage(Chat.format("set firstlocation to §6" + p.getLocation().getBlockX() + "§7.§6" + p.getLocation().getBlockY() + "§7.§6" + p.getLocation().getBlockZ()));
                    }
                }
            }
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 2) {
            return actions.stream().filter(action -> action.startsWith(args[1])).toList();
        }
        return Collections.emptyList();
    }
}
