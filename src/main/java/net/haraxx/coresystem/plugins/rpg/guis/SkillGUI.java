package net.haraxx.coresystem.plugins.rpg.guis;

import net.haraxx.coresystem.plugins.rpg.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

import static net.haraxx.coresystem.builder.Chat.format;

public class SkillGUI {

    Map<Enchantment, Integer> enchants;

    private void open(Player player) {

        enchants.put(Enchantment.CHANNELING, 62);
        enchants.put(Enchantment.IMPALING, 100);

        Inventory inv = new InventoryBuilder(6)
                .buildBottomRow()
                .addItem(new ItemBuilder(Material.IRON_AXE).addNBTTag("protected", "true").build())
                .build();

        ItemStack item = new ItemBuilder(Material.BARRIER)
                .enchant(enchants)
                .durability((short) 1)
                .setProtected(true)
                .addNBTTag("protected", "true")
                .build();

        player.openInventory(inv);
        player.sendMessage(format("casted inventory to Player " + player.getName()));
    }


}
