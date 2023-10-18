package net.haraxx.coresystem;

import net.haraxx.coresystem.configs.PlayerConfig;
import net.haraxx.coresystem.configs.zoll.LocationConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoreSystem extends JavaPlugin {

    private static CoreSystem instance;

    public static CoreSystem getInstance() {
        return instance;
    }

    private static void setInstance(CoreSystem plugin) {
        instance = plugin;
    }

    private LocationConfig locationConfig;

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {
    }

    private void init() {
        try {
            setInstance(this);
            locationConfig = new LocationConfig();
            PlayerConfig.initiatePlayers();
        } catch (Exception e) {
            System.out.println("A fatal error occurred while initialising the api. Exiting...");
        }
    }

    public LocationConfig getLocationConfig() { return locationConfig; }

}