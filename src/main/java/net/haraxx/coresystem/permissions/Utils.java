package net.haraxx.coresystem.permissions;

import org.bukkit.entity.Player;

public class Utils {

    public static boolean getDefaultPerms(Player p) {
        return p.hasPermission("group.developer") || p.isOp();
    }

    public static boolean isSupporter(Player p) {
        return p.hasPermission("group.supporter") || p.isOp() || getDefaultPerms(p);
    }

    public static boolean isVerified(Player p) {
        return !p.hasPermission("group.Unverified") || getDefaultPerms(p);
    }
}