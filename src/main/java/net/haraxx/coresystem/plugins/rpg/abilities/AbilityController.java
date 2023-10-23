package net.haraxx.coresystem.plugins.rpg.abilities;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityController implements Listener {

    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (e.getAction() == Action.RIGHT_CLICK_AIR)
            if (item.hasItemMeta())
                Abilities.getAbilitys().get(Abilities.getAbilityByItemStack(item)).runAbility(p);
    }
}