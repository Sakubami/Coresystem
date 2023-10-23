package net.haraxx.coresystem.plugins.zoll;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerConfig {

    private static Set<UUID> list = new HashSet<>();

    public static boolean getPlayerStatusByUUID(UUID uuid) {
        return list.contains(uuid);
    }

    public static void setPlayerStatus(UUID uuid, boolean value) {
        if (value)
            list.add(uuid);
        else list.remove(uuid);
    }

}
