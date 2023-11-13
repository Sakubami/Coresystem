package net.haraxx.coresystem.api.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Juyas
 * @version 12.11.2023
 * @since 12.11.2023
 */
public interface TabCompletion
{

    TabCompletion NONE = Collections::emptyList;
    TabCompletion PLAYERS = () -> Bukkit.getOnlinePlayers().stream().map( Player::getName ).toList();
    TabCompletion MATERIAL = () -> Arrays.stream( Material.values() ).map( Material::name ).toList();
    TabCompletion MATERIAL_KEYS = () -> Arrays.stream( Material.values() ).map( mat -> mat.getKey().toString() ).toList();

    List<String> tabOptions();

    default TabCompletion concat( TabCompletion completion )
    {
        return () -> {
            List<String> options = new ArrayList<>( this.tabOptions() );
            options.addAll( completion.tabOptions() );
            return options;
        };
    }

}
