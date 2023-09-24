package net.haraxx.coresystem.api.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemCtrl {
    public static ItemStack getItem(Material mat, String name, int modelData, String lore1, String lore2, String lore3) {
        boolean x = false;
        List<String> lore = new ArrayList<>();
        ItemStack stack = new ItemStack(mat);
        ItemMeta meta = stack.getItemMeta();

        meta.setDisplayName(name);
        if (modelData != 0) meta.setCustomModelData(modelData);
        if (lore1 != null) { lore.add(lore1); x = true; }
        if (lore2 != null) { lore.add(lore2); x = true; }
        if (lore3 != null) { lore.add(lore3); x = true; }
        if (x) meta.setLore(lore);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
        // !!! please use CustomModelData in steps of 1 starting at 100 !!!
    }

    public static ItemStack getBasic(Material mat, String name) {
        ItemStack stack = new ItemStack(mat);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return stack;
    }
}
