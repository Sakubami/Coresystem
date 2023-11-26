package net.haraxx.coresystem.api.services;

import org.bukkit.Bukkit;

/**
 * @author Juyas
 * @version 26.11.2023
 * @since 26.11.2023
 */
public enum PluginDependency
{
    LUCK_PERMS( "LuckPerms" ),
    SIMPLE_CLANS( "SimpleClans" ),
    MULTIVERSE_CORE( "Multiverse-Core" );

    private final String pluginName;

    PluginDependency( String pluginName )
    {
        this.pluginName = pluginName;
    }

    public String getPluginName()
    {
        return pluginName;
    }

    public boolean isLoaded()
    {
        return Bukkit.getPluginManager().isPluginEnabled( pluginName );
    }

}
