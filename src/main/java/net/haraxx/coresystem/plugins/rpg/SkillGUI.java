package net.haraxx.coresystem.plugins.rpg;

import net.haraxx.coresystem.builder.InventoryBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static net.haraxx.coresystem.builder.Chat.format;

public class SkillGUI {

    private void open(Player player) {

        Inventory inv = new InventoryBuilder(6)
                .buildBottomRow()
                .setItem(new ItemStack(Material.BARRIER), 5)
                .fillEmpty()
                .build();

        player.openInventory(inv);
        player.sendMessage(format("casted inventory to Player " + player.getName()));
    }


}
