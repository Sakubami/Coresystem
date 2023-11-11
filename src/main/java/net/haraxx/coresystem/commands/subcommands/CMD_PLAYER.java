package net.haraxx.coresystem.commands.subcommands;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.commands.CommandRunner;
import net.haraxx.coresystem.configs.WorldSpawnConfig;
import net.haraxx.coresystem.permissions.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CMD_PLAYER implements CommandRunner {

    @Override
    public void runCommand(CommandSender exe, String[] args) {
        Player p = (Player) exe;
        if (Utils.getDefaultPerms(p)) {
            switch (args[1]) {
                case "gm" -> {
                    if (args.length == 3) {
                        switch (args[2]) {
                            case "0" -> {
                                p.setGameMode(GameMode.SURVIVAL);
                                p.sendMessage(Chat.format("Set own game mode to §6Survival"));
                            }
                            case "1" -> {
                                p.setGameMode(GameMode.CREATIVE);
                                p.sendMessage(Chat.format("Set own game mode to §6Creative"));
                            }
                            case "2" -> {
                                p.setGameMode(GameMode.ADVENTURE);
                                p.sendMessage(Chat.format("Set own game mode to §6Adventure"));
                            }
                            case "3" -> {
                                p.setGameMode(GameMode.SPECTATOR);
                                p.sendMessage(Chat.format("Set own game mode to §6Spectator"));
                            }
                        }
                    }
                    if (args.length == 4) {
                        Player target = Bukkit.getPlayer(args[3]);
                        if (target != null) {
                            switch (args[2]) {
                                case "0" -> {
                                    target.setGameMode(GameMode.SURVIVAL);
                                    p.sendMessage(Chat.format("Set game mode §6Survival §7for §6" + args[3]));
                                }
                                case "1" -> {
                                    target.setGameMode(GameMode.CREATIVE);
                                    p.sendMessage(Chat.format("Set game mode §6Creative §7for §6" + args[3]));
                                }
                                case "2" -> {
                                    target.setGameMode(GameMode.ADVENTURE);
                                    p.sendMessage(Chat.format("Set game mode §6Adventure §7for §6" + args[3]));
                                }
                                case "3" -> {
                                    target.setGameMode(GameMode.SPECTATOR);
                                    p.sendMessage(Chat.format("Set game mode §6Spectator §7for §6" + args[3]));
                                }
                            }
                        } else { p.sendMessage(Chat.format("Spieler mit dem namen §6" + args[1] + " §7konnte nicht gefunden werden"));}
                    }
                }
            }
        } else  p.sendMessage(Chat.format("du hast nicht die nötigen §6Rechte §7um den §6Command §7auszuführen"));
    }

    @Override
    public List<String> tabComplete(CommandSender exe, String[] args) {
        if (args.length == 2) {
            return List.of("gm");
        }

        if (args.length == 3) {
            switch (args[1]) {
                case "gm" -> {
                    return List.of("0", "1", "2", "3");
                }
            }
        }

        if (args.length == 4) {
            return null;
        }

        return Collections.emptyList();
    }
}
