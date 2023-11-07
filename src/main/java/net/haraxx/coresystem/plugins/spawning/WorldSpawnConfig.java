package net.haraxx.coresystem.plugins.spawning;

import net.haraxx.coresystem.CoreSystem;
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
        spawnLocation = config.getLocation("firstSpawn");
        worldSpawnLocation = config.getLocation("worldSpawn");
    }

    public void saveLocations() {
        FileConfiguration config = new YamlConfiguration();
        config.set("firstSpawn", spawnLocation);
        config.set("worldSpawn", worldSpawnLocation);
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

    public static WorldSpawnConfig get() {
        return CoreSystem.getInstance().getWorldSpawnLocation();
    }
}
