package net.haraxx.coresystem.plugins.rpg.abilities.abilities;

import com.comphenix.protocol.PacketType;
import net.haraxx.coresystem.plugins.rpg.abilities.AbilityRunner;
import org.bukkit.Chunk;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class RDM_RESOLUTION implements AbilityRunner, Listener {

    @Override
    public void runAbility(Player player) {
        Chunk chunk = player.getWorld().getChunkAt(player.getLocation());

        Entity armorstand = player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        for (Entity entity: armorstand.getNearbyEntities(3,3,3)) {
            entity.remove();
            armorstand.remove();
        }
        AreaEffectCloud cloud = (AreaEffectCloud) player.getWorld().spawnEntity(player.getLocation(), EntityType.AREA_EFFECT_CLOUD);
        cloud.setCustomName("trigger_one");
    }

    @EventHandler
    public void onEntityInteract(AreaEffectCloudApplyEvent e) {
        if (e.getEntity().getName().equalsIgnoreCase("trigger_one")) {
            for (LivingEntity entity : e.getAffectedEntities()) {
                if (entity instanceof Player p) {
                    p.sendMessage("abc");
                }
            }
        }
    }
}
