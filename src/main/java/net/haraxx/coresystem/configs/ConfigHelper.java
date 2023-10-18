package net.haraxx.coresystem.configs;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;

public class ConfigHelper {
    private static final String path = "plugins/HaraxxCore/Config.yml";

    public void setEnabled(boolean status) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path));
        config.set("enabled", status);
        try {
            config.save(new File(path));
        } catch (Exception ignored) {}
    }

    public boolean loadEnabled() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path));
        return Boolean.parseBoolean(config.getString("enabled"));
    }

    public boolean isEnabled() {
        return loadEnabled();
    }
}
