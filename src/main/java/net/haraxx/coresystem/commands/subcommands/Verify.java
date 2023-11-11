package net.haraxx.coresystem.commands.subcommands;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.commands.CommandRunner;
import net.haraxx.coresystem.permissions.Utils;
import net.haraxx.coresystem.configs.WorldSpawnConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Verify implements CommandRunner {

    LuckPerms api = LuckPermsProvider.get();

    @Override
    public void runCommand(CommandSender exe, String[] args) {
        Player p = (Player) exe;
        if (args.length == 2) {
            if (Bukkit.getPlayer(args[1]) != null) {
                Player target = Bukkit.getPlayer(args[1]);
                if (Utils.isSupporter(p)) {
                    User user = api.getUserManager().getUser(target.getUniqueId());
                    user.data().remove(Node.builder("group.unverified").build());
                    user.data().add(Node.builder("group.default").build());
                    user.data().add(Node.builder("group.i").build());
                    api.getUserManager().saveUser(user);

                    target.teleport(WorldSpawnConfig.get().getWorldSpawnLocation());
                    target.sendMessage(Chat.format("du wurdest §aFreigeschaltet§7. §6Viel Spass auf unserem Server!"));
                } else  p.sendMessage(Chat.format("du hast nicht die nötigen §6Rechte §7um den §6Command §7auszuführen"));
            } else  p.sendMessage(Chat.format("Spieler mit dem namen §6" + args[1] + " §7konnte nicht gefunden werden"));
        } else  p.sendMessage(Chat.format("Bitte einen gültigen §6Spielernamen §7angeben"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
