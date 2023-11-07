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
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Unverify implements CommandRunner {

    LuckPerms api = LuckPermsProvider.get();

    @Override
    public void runCommand(CommandSender exe, String[] args) {
        Player p = (Player) exe;
        if (args.length == 2) {
            if (Bukkit.getPlayer(args[1]) != null) {
                Player target = Bukkit.getPlayer(args[1]);
                if (Utils.getDefaultPerms(p)) {
                    User user = api.getUserManager().getUser(target.getUniqueId());
                    user.data().remove(Node.builder("group.default").build());
                    user.data().add(Node.builder("group.unverified").build());
                    api.getUserManager().saveUser(user);

                    Location loc = WorldSpawnConfig.get().getFirstSpawnLocation();
                    target.teleport(new Location(Bukkit.getServer().getWorld("Rom"), loc.getBlockX() + 0.5, loc.getBlockY() + 0.125, loc.getBlockZ() + 0.5, -90  , 0));
                    target.sendMessage(Chat.format("du wurdest §cGesperrt§7."));
                    target.sendMessage(Chat.format("öffne im §9Discord §7ein Support ticket mit einem §6Screenshot dieser Nachricht"));
                    target.sendMessage(Chat.format("ausgeführt von §6" + exe.getName()));
                } else  p.sendMessage(Chat.format("du hast nicht die nötigen §6Rechte §7um den §6Command §7auszuführen"));
            } else  p.sendMessage(Chat.format("Spieler mit dem namen §6" + args[1] + " §7konnte nicht gefunden werden"));
        } else  p.sendMessage(Chat.format("Bitte einen gültigen §6Spielernamen §7angeben"));
    }

    @Override
    public List<String> tabComplete(String[] args) {
        return null;
    }
}
