package net.haraxx.coresystem.plugins.rpg.abilities.abilities;

import net.haraxx.coresystem.plugins.rpg.abilities.AbilityRunner;
import org.bukkit.Chunk;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.AreaEffectCloudApplyEvent;

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
}
