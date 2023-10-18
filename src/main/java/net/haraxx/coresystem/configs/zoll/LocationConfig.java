package net.haraxx.coresystem.configs.zoll;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class LocationConfig {

    private static class LocEntry {

        private final Location location;
        private final BlockFace direction;
        private final UUID owner;
        private boolean busy;
        private UUID status;

        public LocEntry(Location location, BlockFace direction, UUID owner) {
            this.location = location;
            this.direction = direction;
            this.owner = owner;
            this.busy = false;
            this.status = null;
        }

        public Location getLocation() { return location; }
        public BlockFace getDirection() { return direction; }
        public UUID getOwner() { return owner; }
        public UUID getStatus() { return status; }
        public boolean isBusy() { return busy; }

        public void setBusy(boolean busy) { this.busy = busy; }
        public void setStatus(UUID status) { this.status = status; }
    }

    private static final String path = "plugins/HaraxxCore/Zoll/Locations.yml";

    private ArrayList<LocEntry> locEntries;

    public LocationConfig() {
        locEntries = new ArrayList<>();
        loadLocations();
    }

    public void deleteLoc(Location formattedLoc) {
        this.locEntries.removeIf(locEntry -> locEntry.getLocation().equals(formattedLoc));
        saveLocations();
    }

    public void addLocation(Location loc, Player p, String owner) {
       this.locEntries.add(new LocEntry(loc, p.getFacing(), Bukkit.getOfflinePlayer(owner).getUniqueId()));
    }

    public void setBusy(Location loc, boolean busy) {
        for (LocEntry locEntry : this.locEntries) {
            if (locEntry.getLocation().equals(loc)) {
                locEntry.setBusy(busy);
                return;
            }
        }
    }

    public void setStatus(Location loc, UUID uuid) {
        for (LocEntry locEntry : this.locEntries) {
            if (locEntry.getLocation().equals(loc)) {
                locEntry.setStatus(uuid);
                return;
            }
        }
    }

    public void loadLocations() {
        locEntries = new ArrayList<>();

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path));

        for (int i = 0; true; i++) {
            if (!config.contains("stations." + i)) break;

            String str = config.getString("stations."+i+".location");
            Location loc = stringToLoc(str);
            UUID owner = UUID.fromString(config.getString("stations."+i+".owner"));
            BlockFace dir = BlockFace.valueOf(config.getString("stations."+i+".direction"));

            locEntries.add(new LocEntry(loc, dir, owner));
        }
    }

    public void saveLocations() {

        FileConfiguration config = new YamlConfiguration();

        for (int i = 0; locEntries.size() > i; i++) {
            config.set("stations."+i+".location", locToString(this.locEntries.get(i).getLocation()));
            config.set("stations."+i+".owner", this.locEntries.get(i).getOwner().toString());
            config.set("stations."+i+".direction", this.locEntries.get(i).getDirection().name());
        } try {
            config.save(new File(path));
        } catch (Exception ignored) { }
    }

    public boolean compareClick(Location loc) {
        return locEntries.stream().anyMatch(locEntry -> locEntry.getLocation().equals(loc));
    }

    public BlockFace getDirection(Location formattedLoc) {
        return locEntries.stream().filter(locEntry -> locEntry.getLocation().equals(formattedLoc)).findFirst().get().getDirection();
    }

    private String locToString(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        return x+"/"+y+"/"+z+"/"+loc.getWorld().getName();
    }

    private Location stringToLoc(String str) {
        String[] parts = str.split("/");
        return new Location(Bukkit.getServer().getWorld(parts[3]), Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    public String fixPlayerLocation(Location loc, Player p) {
        int x = loc.getBlockX() + p.getFacing().getModX();
        int y = loc.getBlockY() +1;
        int z = loc.getBlockZ() + p.getFacing().getModZ();
        return x+"/"+y+"/"+z+"/"+loc.getWorld().getName();
    }
}
