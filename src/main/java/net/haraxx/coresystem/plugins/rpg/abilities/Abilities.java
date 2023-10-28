package net.haraxx.coresystem.plugins.rpg.abilities;

import net.haraxx.coresystem.plugins.rpg.abilities.abilities.BSK_tomahawk;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Abilities implements Listener {

    private static final HashMap<String, AbilityRunner> abilitys = new HashMap<>();

    public static void addAbilities() {
        abilitys.put("BSK_TOMAHAWK", new BSK_tomahawk());
    }

    public static String getAbilityByItemStack(ItemStack item) {
        if (item.getItemMeta() != null) {
            List<String> lore = item.getItemMeta().getLore();
            if (lore != null)
                for (String key : abilitys.keySet()) {
                    if (lore.contains(key))
                        return key;
                }
        }
        return null;
    }

    public static HashMap<String, AbilityRunner> getAbilitys() {
        return abilitys;
    }

    public static ArrayList<String> getAbilityNames() {
        return new ArrayList<>(abilitys.keySet());
    }
}
