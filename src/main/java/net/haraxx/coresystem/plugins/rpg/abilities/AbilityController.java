package net.haraxx.coresystem.plugins.rpg.abilities;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.builder.item.NBTapi;
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
            if (item.hasItemMeta() && nbt.getNBTTagValue(item, "ability") != null)
                Abilities.getAbilitys().get(nbt.getNBTTagValue(item, "ability")).runAbility(p);
            else p.sendMessage(Chat.format("Diese Waffe hat keine Fähigkeit!"));
    }
}