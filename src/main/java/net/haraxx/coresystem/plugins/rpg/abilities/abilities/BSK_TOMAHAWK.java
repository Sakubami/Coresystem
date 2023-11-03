package net.haraxx.coresystem.plugins.rpg.abilities.abilities;

import net.haraxx.coresystem.CoreSystem;
import net.haraxx.coresystem.builder.item.ItemBuilder;
import net.haraxx.coresystem.plugins.rpg.abilities.AbilityRunner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class BSK_TOMAHAWK implements AbilityRunner, Listener {
    private ArrayList<Integer> ids = new ArrayList<>();

    @Override
    public void runAbility(Player player) {
        World world = player.getWorld();
        Entity entity = world.spawnEntity(player.getLocation(), EntityType.ARROW);
        Vector v = player.getEyeLocation().getDirection();
        entity.setVelocity(v.multiply(1));
        entity.setGravity(false);
        entity.setCustomName("test");
        ItemStack item = new ItemBuilder(Material.IRON_AXE).build();

        double x = v.getX();
        double y = v.getY();
        double z = v.getZ();
        Location spawningLocation = new Location(world,x,y,z);

        Entity armorstand = world.spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        ArmorStand stand = (ArmorStand) armorstand;
        stand.setGravity(false);
        stand.setArms(true);
        stand.setRightArmPose(new EulerAngle(Math.toRadians(90), 0, 0));
        stand.setLeftArmPose(new EulerAngle(0, 0, Math.toRadians(270)));
        stand.setRightArmPose(new EulerAngle(0, 0, Math.toRadians(90)));

        BukkitScheduler scheduler = Bukkit.getScheduler();
        this.ids.add(scheduler.scheduleSyncRepeatingTask(CoreSystem.getInstance(), () -> {
            player.sendMessage("test");
            spawningLocation.toVector().multiply(1);

        },0,10));

        scheduler.runTaskLater(CoreSystem.getInstance(), bukkitTask -> {
            int firstId = ids.get(0);
            scheduler.cancelTask(firstId);
            ids.remove(firstId);
            player.sendMessage("cancel");
                },160);
    }

    @EventHandler
    public void onEntityInteract(ProjectileHitEvent e) {
        LivingEntity entity = (LivingEntity) e.getHitEntity();
        if (entity != null) {
            entity.damage(10);
            e.getEntity().remove();
        }
        if (e.getHitBlock() != null) {
            e.getEntity().remove();
        }
    }
}