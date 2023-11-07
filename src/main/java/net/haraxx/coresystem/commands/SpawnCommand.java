package net.haraxx.coresystem.commands;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.permissions.Utils;
import net.haraxx.coresystem.configs.WorldSpawnConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("spawn")) {
            Player player = (Player) commandSender;
            if (player.getWorld().getName().equalsIgnoreCase("rom"))
                if (Utils.isVerified(player)) {
                    player.teleport(WorldSpawnConfig.get().getWorldSpawnLocation());
                    player.sendMessage(Chat.format("Teleporting..."));
                }
        }
        return false;
    }
}
