package net.haraxx.coresystem.permissions;

import org.bukkit.entity.Player;

public class Utils {

    public static boolean getDefaultPerms(Player p) {
        return p.hasPermission("group.developer") || p.isOp();
    }

    public static boolean isSupporter(Player p) {
        return p.hasPermission("group.supporter") || getDefaultPerms(p);
    }

    public static boolean isVerified(Player p) {
        if (p.isOp())
            return true;
       return !p.hasPermission("group.unverified");
    }

    public static boolean isNew(Player p) {
        if (p.isOp())
            return false;
        return p.hasPermission("group.unverified");
    }
}