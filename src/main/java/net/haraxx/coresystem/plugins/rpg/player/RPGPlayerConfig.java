package net.haraxx.coresystem.plugins.rpg.player;

import net.haraxx.coresystem.CoreSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RPGPlayerConfig {

    private static class RPGPlayer {

        //general information
        protected UUID uuid;

        //general stats
        protected int level;

        //stats
        protected double health;
        protected double defense;
        protected double strength;
        protected double critDamage;
        protected double intelligence;

        protected double critChance;

        //classes
        protected String playerClass;
        protected int class_RDM;

        public RPGPlayer(UUID uuid, String playerClass, int level, double health, double defense, double strength, double critDamage, double intelligence,  double critChance, int class_RDM) {
            this.uuid = uuid;
            this.playerClass = playerClass;
            this.level = level;
            this.health = health;
            this.defense = defense;
            this.strength = strength;
            this.critDamage = critDamage;
            this.intelligence = intelligence;
            this.critChance = critChance;
            this.class_RDM = class_RDM;
        }

        public UUID getUUID() { return uuid; }
        public String getPlayerClass() { return playerClass; }
        public int getLevel() { return level; }
        public double getHealth() { return health; }
        public double getDefense() { return defense; }
        public double getStrength() { return strength; }
        public double getCritDamage() { return critDamage; }
        public double getIntelligence() { return intelligence; }
        public double getCritChance() { return critChance; }
        public int getRDMLevel() { return class_RDM; }

        public void setLevel(int level) { this.level = level; }
        public void setPlayerClass(String playerClass) { this.playerClass = playerClass; }
        public void setHealth(double health) { this.health = health; }
        public void setDefense(double defense) { this.defense = defense; }
        public void setStrength(double strength) { this.strength = strength; }
        public void setCritDamage(double critDamage) { this.critDamage = critDamage; }
        public void setIntelligence(double intelligence) { this.intelligence = intelligence; }
        public void setCritChance(double critChance) { this.critChance = critChance; }
        public void setRDMLevel(int level) { this.class_RDM = level; }
    }

    private final String path = "plugins/HaraxxCore/RPG/Player.yml";

    private ArrayList<RPGPlayer> rpgPlayers;

    public RPGPlayerConfig() {
        rpgPlayers = new ArrayList<>();
        loadPlayers();
    }

    public void autoSave() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CoreSystem.getInstance(), this::savePlayers, 1, 1200);
    }

    public void loadPlayers() {
        rpgPlayers = new ArrayList<>();

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path));

        for (int i = 0; true; i++) {
            if (!config.contains("player." + i)) break;

            UUID uuid = UUID.fromString(config.getString("player."+i+".uuid"));
            String playerClass = config.getString("player."+i+".playerClass");
            int level = config.getInt("player."+i+".level");
            double health = config.getDouble("player."+i+".health");
            double defense = config.getDouble("player."+i+".defense");
            double strength = config.getDouble("player."+i+".strength");
            double critDamage = config.getDouble("player." + i + ".critDamage");
            double intelligence = config.getDouble("player." + i + ".intelligence");
            double critChance = config.getDouble("player."+i+".critChance");
            int RDMLevel = config.getInt("player."+i+".class_RDM");

            rpgPlayers.add(new RPGPlayer(uuid, playerClass, level, health, defense, strength, critDamage, intelligence, critChance, RDMLevel));
        }
    }

    public void savePlayers() {

        FileConfiguration config = new YamlConfiguration();

        for (int i = 0; rpgPlayers.size() > i; i++) {
            config.set("player."+i+".uuid", rpgPlayers.get(i).getUUID().toString());
            config.set("player."+i+".playerClass", rpgPlayers.get(i).getPlayerClass());
            config.set("player."+i+".level", rpgPlayers.get(i).getLevel());
            config.set("player."+i+".health", rpgPlayers.get(i).getHealth());
            config.set("player."+i+".defense", rpgPlayers.get(i).getDefense());
            config.set("player."+i+".strength", rpgPlayers.get(i).getStrength());
            config.set("player."+i+".critDamage", rpgPlayers.get(i).getCritDamage());
            config.set("player."+i+".intelligence", rpgPlayers.get(i).getIntelligence());
            config.set("player."+i+".critChance", rpgPlayers.get(i).getCritChance());
            config.set("player."+i+".class_RDM", rpgPlayers.get(i).getRDMLevel());
        } try {
            config.save(new File(path));
        } catch (Exception ignored) { }
    }

    public void addNewPlayer(Player p) {
        if (getRPGPlayer(p) == null) {
            rpgPlayers.add(new RPGPlayer(p.getUniqueId(), "DEFAULT", 1, 100, 0, 0, 0, 100, 40, 1));
            savePlayers();
        }
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
    }

    public void setPlayerClass(Player player, String playerClass) {
        getRPGPlayer(player).setPlayerClass(playerClass);
    }

    public void setHealth(Player player, double value) {
        getRPGPlayer(player).setHealth(value);
    }

    public void setDefense(Player player, double value) {
        getRPGPlayer(player).setDefense(value);
    }

    public void setStrength(Player player, double value) {
        getRPGPlayer(player).setStrength(value);
    }

    public void setCritDamage(Player player, double value) {
        getRPGPlayer(player).setCritDamage(value);
    }

    public void setIntelligence(Player player, double value) {
        getRPGPlayer(player).setIntelligence(value);
    }

    public void setCritChance(Player player, double value) {
        getRPGPlayer(player).setCritChance(value);
    }

    public void setRDMLevel(Player player, int value) {
        getRPGPlayer(player).setRDMLevel(value);
    }

    public int getLevel(Player player) {
        return getRPGPlayer(player).getLevel();
    }

    public String getPlayerClass(Player player) {
        return getRPGPlayer(player).getPlayerClass();
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

    public double getIntelligence(Player player) { return getRPGPlayer(player).getIntelligence(); }

    public double getCritChance(Player player) { return getRPGPlayer(player).getCritChance(); }

    public int getRDMLevel(Player player) { return getRPGPlayer(player).getRDMLevel(); }

    public ArrayList<String> getPlayerAttributes(Player player) {
        ArrayList<String> list = new ArrayList<>();
        list.add("level: "+ getLevel(player));
        list.add("playerClass: "+ getPlayerClass(player));
        list.add("health: "+ getHealth(player));
        list.add("defense: "+ getDefense(player));
        list.add("strength: "+ getStrength(player));
        list.add("critDamage: "+ getCritDamage(player));
        list.add("intelligence: "+ getIntelligence(player));
        list.add("critChance: "+ getCritChance(player));
        list.add("RDMLevel: "+ getRDMLevel(player));
        return list;
    }

    public static RPGPlayerConfig get() {
        return CoreSystem.getInstance().getRPGPlayerConfig();
    }
}
