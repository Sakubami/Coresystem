package net.haraxx.coresystem.plugins.spawning;

import net.haraxx.coresystem.CoreSystem;
import net.haraxx.coresystem.builder.Chat;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;

public class PlayerSpawn implements Listener {

    LuckPerms api = LuckPermsProvider.get();
    private String path(String path) { return "plugins/HaraxxCore/Core/Spawn/LastPlayerLocations/" + path + ".yml"; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        boolean value = p.hasPlayedBefore();

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(path(p.getUniqueId().toString())));
        Location location = config.getLocation("location");

        if (location != null && value) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CoreSystem.getInstance(), () -> {
                p.teleport(new Location(p.getWorld(), location.getX(), location.getY() + 0.1, location.getZ(), location.getYaw(), location.getPitch()));
                p.sendMessage(Chat.format("teleported to last Location §6" + location.getBlockX() +"§7.§6"+ location.getBlockY() +"§7.§6"+ location.getBlockZ()));
            }, 2);
        }

        if (!value) {
            Location loc = WorldSpawnConfig.get().getFirstSpawnLocation();

            NewPlayerConfig.get().addNewPlayer(p);

            User user = api.getUserManager().getUser(p.getUniqueId());
            user.data().add(Node.builder("group.unverified").build());
            user.data().remove(Node.builder("group.default").build());
            api.getUserManager().saveUser(user);

            Bukkit.getScheduler().scheduleSyncDelayedTask(CoreSystem.getInstance(), () -> {
                p.teleport(new Location(Bukkit.getServer().getWorld("Rom"), loc.getBlockX() + 0.5, loc.getBlockY() + 0.125, loc.getBlockZ() + 0.5, -90  , 0));
            }, 2);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        FileConfiguration config = new YamlConfiguration();

        config.set("location", p.getLocation());

        try {
        config.save(new File(path(p.getUniqueId().toString())));
        } catch (Exception ignored) { }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (p.getWorld().getName().equalsIgnoreCase("rom")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(CoreSystem.getInstance(), () -> {
                p.teleport(WorldSpawnConfig.get().getWorldSpawnLocation());
                p.sendMessage(Chat.format("respawning..."));
            },  1);
        }
    }

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (NewPlayerConfig.get().isNew(p.getUniqueId()))
                e.setCancelled(true);
        }
    }
}
