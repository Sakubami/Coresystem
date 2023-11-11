package net.haraxx.coresystem.commands.subcommands;

import net.haraxx.coresystem.builder.item.ItemBuilder;
import net.haraxx.coresystem.commands.CommandRunner;
import net.haraxx.coresystem.permissions.Utils;
import net.haraxx.coresystem.plugins.rpg.player.RPGPlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CMD_RPG implements CommandRunner {

    private final List<String> actions = List.of("player", "item");
    private final List<String> actionsPlayer = List.of("info");
    private final List<String> actionsItem = List.of("register", "get", "list");
    @Override
    public void runCommand(CommandSender exe, String[] args) {

        Player p = (Player) exe;
        RPGPlayerConfig rpgPlayerConfig = RPGPlayerConfig.get();

        if (Utils.getDefaultPerms(p)) {
            switch (args[1]) {
                case "player" -> {
                    switch (args[2]) {
                        case "info" -> {
                            if (Bukkit.getPlayer(args[3]) != null) {
                                Player target = Bukkit.getPlayer(args[3]);
                                if (rpgPlayerConfig.getRPGPlayer(p) != null) {
                                    for (String str: rpgPlayerConfig.getPlayerAttributes(target)) {
                                        p.sendMessage(str);
                                    }
                                }
                            }
                        }
                    }
                }

                case "item" -> {
                    switch (args[2]) {
                        case "register" -> {
                            // add new item
                        }

                        case "get" -> {
                            String id = args[3];
                            p.getInventory().addItem(new ItemBuilder(Material.getMaterial(id.toUpperCase())).build());
                            // get already existing item by id
                        }

                        case "list" -> {
                            // get all existing items
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return actions.stream().filter(action -> action.startsWith(args[1])).toList();
        }

        if (args.length == 3) {
            switch (args[1]) {
                case "player" -> {
                    return actionsPlayer.stream().filter(action -> action.startsWith(args[2])).toList();
                }

                case "item" -> {
                    return actionsItem.stream().filter(action -> action.startsWith(args[2])).toList();
                }
            }

        }

        if (args.length == 4) {
            switch (args[2]) {
                case "get" -> {
                    return List.of("item1", "item2", "item3");
                }

                case "list" -> {
                    //get items
                    return List.of("item1", "item2", "item3", "item4");
                }

                case "add", "info" -> {
                    return null;
                }
            }
        }
        return Collections.emptyList();
    }
}
