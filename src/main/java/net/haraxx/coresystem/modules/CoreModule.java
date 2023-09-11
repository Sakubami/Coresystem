package net.haraxx.coresystem.modules;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CoreModule extends JavaPlugin {
    public CoreModule module;

    public CoreModule getModule() {
        return module;
    }
}
