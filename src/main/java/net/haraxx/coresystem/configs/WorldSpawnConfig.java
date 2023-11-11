package net.haraxx.coresystem.configs;

import net.haraxx.coresystem.CoreSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class WorldSpawnConfig {
    private final String path = "plugins/HaraxxCore/Core/Spawn/SpawnLocation.yml";

    private Location spawnLocation;
    private Location worldSpawnLocation;

    public WorldSpawnConfig() {
        loadLocation();
    }

    public void loadLocation() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path));
        if (config.getString("firstSpawn") != null) {
            spawnLocation = stringToLoc(config.getString("firstSpawn"));
            worldSpawnLocation = stringToLoc(config.getString("worldSpawn"));
            System.out.println("FOUND LOCATIONS");
        } else {
            spawnLocation = new Location(Bukkit.getServer().getWorld("test1"), 47, -50, 80);
            worldSpawnLocation = new Location( Bukkit.getServer().getWorld( "test1" ), 3246, 114,  -9981);
            System.out.println("DIDNT FOUND LOCATIONS");
            System.out.println(spawnLocation);
            saveLocations();
        }
    }

    public void saveLocations() {
        FileConfiguration config = new YamlConfiguration();
        config.set("firstSpawn", locToString(spawnLocation));
        config.set("worldSpawn", locToString(worldSpawnLocation));
        try {
            config.save(new File(path));
        } catch (Exception ignored) { }
    }

    public Location getFirstSpawnLocation() {
        return spawnLocation;
    }

    public Location getWorldSpawnLocation() {
        return worldSpawnLocation;
    }

    public void setFirstLocation(Location location) {
        this.spawnLocation = location;
        saveLocations();
    }

    public void setWorldSpawnLocation(Location location) {
        this.worldSpawnLocation = location;
        saveLocations();
    }

    public String locToString(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        float yaw = loc.getYaw();
        float pitch = loc.getPitch();
        return x+"/"+y+"/"+z+"/"+loc.getWorld().getName()+"/"+yaw+"/"+pitch;
    }

    public Location stringToLoc(String str) {
        String[] parts = str.split("/");
        return new Location(Bukkit.getServer().getWorld(parts[3]), Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]));
    }

    public static WorldSpawnConfig get() {
        return CoreSystem.getInstance().getWorldSpawnLocation();
    }
}
