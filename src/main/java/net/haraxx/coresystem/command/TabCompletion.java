package net.haraxx.coresystem.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Juyas
 * @version 12.11.2023
 * @since 12.11.2023
 */
public interface TabCompletion
{

    TabCompletion PLAYERS = () -> Bukkit.getOnlinePlayers().stream().map( Player::getName ).toList();
    TabCompletion MATERIAL = () -> Arrays.stream( Material.values() ).map( Material::name ).toList();

    List<String> tabOptions();

}
