package net.haraxx.coresystem.Configs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConfigHelper {
    private static final String path1 = "plugins/Core/Locations.yml";
    private static final String path2 = "plugins/Core/Items.yml";

    public void addLocation(Location loc) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path1));
        for(int i = 0; true; i++) {
            if(!config.contains("locations."+i)) {

                config.set("locations."+i+".id", String.valueOf(i));
                config.set("locations."+i+".location", loc.getBlockX() + "/" + loc.getBlockY() + "/" + loc.getBlockZ() + "/" + loc.getWorld().getName());
                try {
                    config.save(new File(path1));
                }catch (Exception ignored){}
                return;
            }
        }
    }

    public void addItem(ItemStack itemStack) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path2));
        for(int i = 0; true; i++) {
            if(!config.contains("locations."+i)) {

                config.set("locations."+i+".id", String.valueOf(i));
                try {
                    config.save(new File(path2));
                }catch (Exception ignored){}
                return;
            }
        }
    }

    public void deleteLoc(Location loc) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path1));
        String location = loc.getBlockX()+"/"+loc.getBlockY()+"/"+loc.getBlockZ()+"/"+loc.getWorld().getName();
        for(int i = 0; true; i++) {
            if(config.contains("locations."+i)) {
                if (config.getString("locations."+i+".location").equalsIgnoreCase(location)) {
                    config.set("locations."+i+".location", 0 + "/" + 1000 + "/" + 0 + "/" + loc.getWorld().getName());
                    try {
                        config.save(new File(path1));
                    }catch (Exception ignored){}
                    return;
                }
            }
        }
    }

    public static ArrayList<String> loadLocations() {
        ArrayList<String> list = new ArrayList<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path1));

        for (int i = 0; true; i++) {
            if (!config.contains("locations." + i))
                return list;

            String name = config.getString("locations."+i+".id");
            String location = config.getString("locations."+i+".location");
            String line = name+"§"+location;
            list.add(line);
        }
    }

    public boolean checkCLickEqualsLoc(Location loc) {
        for(String list : loadLocations()) {
            String[] str = list.split("%");
            if (str[1].equalsIgnoreCase(convertLocation(loc))) {
                return true;
            }
        }
        return false;
    }

    public static String convertLocation(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        return x+"/"+y+"/"+z+"/"+loc.getWorld().getName();
    }
}
