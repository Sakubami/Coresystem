package net.haraxx.coresystem.commands;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.permissions.Utils;
import net.haraxx.coresystem.configs.WorldSpawnConfig;
import org.bukkit.Location;
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
            if (Utils.isVerified(player)) {
                Location oldLoc = WorldSpawnConfig.get().getWorldSpawnLocation();
                player.teleport(new Location(oldLoc.getWorld(), oldLoc.getBlockX() + 0.5, oldLoc.getBlockY() + 0.125, oldLoc.getBlockZ() + 0.5, 0  , 0));
                player.sendMessage(Chat.format("Teleporting..."));
            }
        }
        return false;
    }
}
