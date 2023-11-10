package net.haraxx.coresystem.plugins.rpg.player;

import net.haraxx.coresystem.CoreSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class RPGPlayerConfig {

    private static class RPGPlayer {

        //general information
        private UUID uuid;

        //general stats
        private int level;

        //stats
        private double health;
        private double defense;
        private double strength;
        private double critDamage;

        //classes
        private int class_RDM;

        public RPGPlayer(UUID uuid, int level, double health, double defense, double strength, double critDamage,  int class_RDM) {
            this.uuid = uuid;
            this.level = level;
            this.health = health;
            this.defense = defense;
            this.strength = strength;
            this.critDamage = critDamage;
            this.class_RDM = class_RDM;
        }

        public UUID getUUID() { return uuid; }
        public int getLevel() { return level; }
        public double getHealth() { return health; }
        public double getDefense() { return defense; }
        public double getStrength() { return strength; }
        public double getCritDamage() { return critDamage; }
        public int getRDMLevel() { return class_RDM; }

        public void setLevel(int level) { this.level = level; }
        public void setHealth(double health) { this.health = health; }
        public void setDefense(double defense) { this.defense = defense; }
        public void setStrength(double strength) { this.strength = strength; }
        public void setCritDamage(double critDamage) { this.critDamage = critDamage; }
        public void setRDMLevel(int level) { this.class_RDM = level; }
    }

    private final String path = "plugins/HaraxxCore/RPG/Player.yml";

    private ArrayList<RPGPlayer> rpgPlayers;

    public RPGPlayerConfig() {
        rpgPlayers = new ArrayList<>();
        loadPlayers();
    }

    public void loadPlayers() {
        rpgPlayers = new ArrayList<>();

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path));

        for (int i = 0; true; i++) {
            if (!config.contains("player." + i)) break;

            UUID uuid = UUID.fromString(config.getString("player."+i+".uuid"));
            int level = config.getInt("player."+i+".level");
            double health = config.getDouble("player."+i+".health");
            double defense = config.getDouble("player."+i+".defense");
            double strength = config.getDouble("player."+i+".strength");
            double critDamage = config.getDouble("player." + i + ".critDamage");
            int RDMLevel = config.getInt("player."+i+".class_RDM");

            rpgPlayers.add(new RPGPlayer(uuid, level, health, defense, strength,critDamage, RDMLevel));
        }
    }

    public void savePlayers() {

        FileConfiguration config = new YamlConfiguration();

        for (int i = 0; rpgPlayers.size() > i; i++) {
            config.set("player."+i+".uuid", rpgPlayers.get(i).getUUID());
            config.set("player."+i+".level", rpgPlayers.get(i).getLevel());
            config.set("player."+i+".health", rpgPlayers.get(i).getHealth());
            config.set("player."+i+".defense", rpgPlayers.get(i).getDefense());
            config.set("player."+i+".strength", rpgPlayers.get(i).getStrength());
            config.set("player."+i+".critDamage", rpgPlayers.get(i).getCritDamage());
            config.set("player."+i+".class_RDM", rpgPlayers.get(i).getRDMLevel());
        } try {
            config.save(new File(path));
        } catch (Exception ignored) { }
    }

    public void updatePlayer(Player p) {

        FileConfiguration loadedConfig = YamlConfiguration.loadConfiguration(new File(path));
        FileConfiguration config = new YamlConfiguration();

        for (int i = 0; rpgPlayers.size() > i; i++) {

            if (UUID.fromString(loadedConfig.getString("player."+i+".uuid")).equals(getRPGPlayer(p).getUUID())) {
                config.set("player."+i+".uuid", rpgPlayers.get(i).getUUID());
                config.set("player."+i+".level", rpgPlayers.get(i).getLevel());
                config.set("player."+i+".health", rpgPlayers.get(i).getHealth());
                config.set("player."+i+".defense", rpgPlayers.get(i).getDefense());
                config.set("player."+i+".strength", rpgPlayers.get(i).getStrength());
                config.set("player."+i+".critDamage", rpgPlayers.get(i).getCritDamage());
                config.set("player."+i+".class_RDM", rpgPlayers.get(i).getRDMLevel());
            } try {
                config.save(new File(path));
            } catch (Exception ignored) { }
        }
    }

    public void addNewPlayer(Player p) {
        rpgPlayers.add(new RPGPlayer(p.getUniqueId(), 1, 100, 0, 0, 0, 1));
        savePlayers();
    }

    public RPGPlayer getRPGPlayer(Player p) {
        for (RPGPlayer rpgPlayer : rpgPlayers) {
            if (rpgPlayer.getUUID().equals(p.getUniqueId())) {
                return rpgPlayer;
            }
        }
        return null;
    }

    public void setLevel(Player player, int value) {
        getRPGPlayer(player).setLevel(value);
        updatePlayer(player);
    }

    public void setHealth(Player player, double value) {
        getRPGPlayer(player).setHealth(value);
        updatePlayer(player);
    }

    public void setDefense(Player player, double value) {
        getRPGPlayer(player).setDefense(value);
        updatePlayer(player);
    }

    public void setStrength(Player player, double value) {
        getRPGPlayer(player).setStrength(value);
        updatePlayer(player);
    }

    public void setCritDamage(Player player, double value) {
        getRPGPlayer(player).setCritDamage(value);
        updatePlayer(player);
    }

    public void setRDMLevel(Player player, int value) {
        getRPGPlayer(player).setRDMLevel(value);
        updatePlayer(player);
    }

    public int getLevel(Player player) {
        return getRPGPlayer(player).getLevel();
    }

    public double getHealth(Player player) {
        return getRPGPlayer(player).getHealth();
    }

    public double getDefense(Player player) {
        return getRPGPlayer(player).getDefense();
    }

    public double getStrength(Player player) {
        return getRPGPlayer(player).getStrength();
    }

    public double getCritDamage(Player player) { return getRPGPlayer(player).getCritDamage(); }

    public int getRDMLevel(Player player) {
        return getRPGPlayer(player).getRDMLevel();
    }

    public static RPGPlayerConfig get() {
        return CoreSystem.getInstance().getRPGPlayerConfig();
    }
}
