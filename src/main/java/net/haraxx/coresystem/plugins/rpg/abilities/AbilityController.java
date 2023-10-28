package net.haraxx.coresystem.plugins.rpg.abilities;

import net.haraxx.coresystem.plugins.rpg.item.NBTapi;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityController implements Listener {
    NBTapi nbt = new NBTapi();

    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (e.getAction() == Action.RIGHT_CLICK_AIR)
            if (item.hasItemMeta())
                Abilities.getAbilitys().get(nbt.getNBTValueByItemStack(item, "ability")).runAbility(p);
    }
}