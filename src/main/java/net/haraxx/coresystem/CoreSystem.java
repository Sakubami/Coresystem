package net.haraxx.coresystem;

import net.haraxx.coresystem.Configs.ConfigHelper;
import net.haraxx.coresystem.Configs.PlayerConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class CoreSystem extends JavaPlugin {

    private static CoreSystem instance;

    public static CoreSystem getInstance() {
        return instance;
    }

    private static void setInstance(CoreSystem plugin) {
        instance = plugin;
    }

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

            PlayerConfig.initiatePlayers();
        } catch (Exception e) {
            System.out.println("A fatal error occurred while initialising the api. Exiting...");
        }
    }
}