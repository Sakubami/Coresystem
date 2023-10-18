package net.haraxx.coresystem.configs;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PlayerConfig {
    private static final Set<OfflinePlayer> players = new HashSet<>();
    private static final String path3 = "plugins/HaraxxCore/Players.yml";

    public static ArrayList<String> loadPlayers() {
        ArrayList<String> list = new ArrayList<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path3));

        for (int i = 0; true; i++) {

            if (!config.contains("players."+i)) return list;

            String name = config.getString("players."+i+".id");
            String status = config.getString("players."+i+".status");
            String display = config.getString("players."+i+".name");

            String line = name+"%"+status+"%"+display;
            list.add(line);
        }
    }

    public static void setPlayerStatus(Player p, boolean status) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path3));
        int i = 0;
        for(String list : loadPlayers()) {
            String[] str = list.split("%");
            if (str[0].contains(p.getUniqueId().toString())) {
                if (status) {
                    config.set("players."+i+".status", true);
                } else {
                    config.set("players."+i+".status", false);
                }

                try {
                    config.save(new File(path3));
                } catch (Exception ignored) {}
                return;
            }
            i++;
        }
    }

    public boolean checkPlayerStatus(Player p) {
        for(String list : loadPlayers()) {
            String[] str = list.split("%");
            if (str[0].contains(p.getUniqueId().toString())) {
                return Boolean.parseBoolean(str[1]);
            }
        }
        return true;
    }


    public static void initiatePlayers() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path3));

        players.addAll(Arrays.asList(Bukkit.getServer().getOfflinePlayers()));
        players.addAll(Bukkit.getServer().getOnlinePlayers());

        int i = 0;
        for(OfflinePlayer offlinePlayer : players) {
            i++;

            if(!config.contains("players."+i)) {

                config.set("players."+i+".id", offlinePlayer.getUniqueId());
                config.set("players."+i+".status", false);
                config.set("players."+i+".name", offlinePlayer.getName());

                try {
                    config.save(new File(path3));
                } catch (Exception ignored) {}
                return;
            }
        }
    }
}
