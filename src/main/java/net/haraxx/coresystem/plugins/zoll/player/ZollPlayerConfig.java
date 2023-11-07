package net.haraxx.coresystem.plugins.zoll.player;

import java.util.ArrayList;
import java.util.UUID;

public class ZollPlayerConfig {

    private static class PlayerEntry {
        private final String name;
        private final UUID uuid;
        private boolean isUsingCustoms = false;

        private PlayerEntry(String name, UUID uuid, boolean isUsingCustoms) {
            this.name = name;
            this.uuid = uuid;
            this.isUsingCustoms = isUsingCustoms;
        }

        public String getName() { return name; }
        public UUID getPlayer() { return uuid; }
        public boolean isUsingCustoms() { return isUsingCustoms; }

        public void setUsingCustoms(boolean value) { this.isUsingCustoms = value; }
    }

    private final String path = "plugins/HaraxxCore/Players.yml";

    private ArrayList<PlayerEntry> playerEntries;

    public ZollPlayerConfig() {
        playerEntries = new ArrayList<>();
    }

    /*
    public void loadPlayers() {
        playerEntries = new ArrayList<>();

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path));

        for (int i = 0; true; i++) {
            if (!config.contains("stations." + i)) break;

            String str = config.getString("stations."+i+".location");
            Location loc = stringToLoc(str);
            UUID owner = UUID.fromString(config.getString("stations."+i+".owner"));
            BlockFace dir = BlockFace.valueOf(config.getString("stations."+i+".direction"));

            playerEntries.add(new ZollPlayerConfig.PlayerEntry(loc, dir, owner));
        }
    }

    public void savePlayers() {

        FileConfiguration config = new YamlConfiguration();

        for (int i = 0; locEntries.size() > i; i++) {
            config.set("stations."+i+".location", locToString(locEntries.get(i).getLocation()));
            config.set("stations."+i+".owner", locEntries.get(i).getOwner().toString());
            config.set("stations."+i+".direction", locEntries.get(i).getDirection().name());
        } try {
            config.save(new File(path));
        } catch (Exception ignored) { }
    }
     */

}
