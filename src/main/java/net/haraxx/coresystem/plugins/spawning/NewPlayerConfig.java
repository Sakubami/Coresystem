package net.haraxx.coresystem.plugins.spawning;

import net.haraxx.coresystem.CoreSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class NewPlayerConfig {

    private final String path = "plugins/HaraxxCore/Core/Spawn/Players.yml";

    private ArrayList<UUID> uuids;

    public NewPlayerConfig() {
        uuids = new ArrayList<>();
        loadLocation();
    }

    public void loadLocation() {
        uuids = new ArrayList<>();

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path));

        for (int i = 0; true; i++) {
            if (!config.contains("players." + i)) break;
            UUID uuid = UUID.fromString(config.getString("players."+i+".uuid"));
            uuids.add(uuid);
        }
    }

    public void savePlayers() {
        FileConfiguration config = new YamlConfiguration();

        for (int i = 0; uuids.size() > i; i++) {
            config.set("players."+i+".uuid", uuids.get(i).toString());
        }
        try {
            config.save(new File(path));
        } catch (Exception ignored) { }
    }

    public boolean isNew(UUID uuid) {
        return uuids.contains(uuid);
    }

    public void removeNewPlayer(Player player) {
        this.uuids.removeIf(uuid -> uuid.equals(player.getUniqueId()));
        savePlayers();
    }

    public void addNewPlayer(Player player) {
        if (!this.uuids.contains(player.getUniqueId()))
            this.uuids.add(player.getUniqueId());
        savePlayers();
    }

    public static NewPlayerConfig get() {
        return CoreSystem.getInstance().getNewPlayerConfig();
    }
}
