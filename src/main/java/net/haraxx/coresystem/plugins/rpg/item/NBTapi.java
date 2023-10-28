package net.haraxx.coresystem.plugins.rpg.item;

import net.haraxx.coresystem.CoreSystem;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class NBTapi {

    private final HashMap<NamespacedKey, String> list = new HashMap<>();
    private NamespacedKey key(String key) { return new NamespacedKey(CoreSystem.getInstance(), key); }

    // dont use outside itembuilder

    public void extractNBTData(ItemMeta meta) {
        for (NamespacedKey key: meta.getPersistentDataContainer().getKeys()) {
            list.put(key, meta.getPersistentDataContainer().get(key, PersistentDataType.STRING));
        }
    }

    public void addAllNBTTagList(HashMap<NamespacedKey, String> list) {
        this.list.putAll(list);
    }

    public void addNBTTag(NamespacedKey key, String value) {
        this.list.put(key, value);
    }

    public ItemMeta parseAllNBTTags(ItemMeta meta) {
        for (NamespacedKey key : this.list.keySet()) {
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.list.get(key));
        }
        return meta;
    }

    //

    public String getNBTValueByItemStack(ItemStack item, String key) {
        return item.getItemMeta().getPersistentDataContainer().get(key(key), PersistentDataType.STRING);
    }

    public HashMap<NamespacedKey, String> getNBTTagsByItemStack(ItemStack item) {
        HashMap<NamespacedKey, String> list = new HashMap<>();
        for (NamespacedKey keySet: item.getItemMeta().getPersistentDataContainer().getKeys()) {
            list.put(keySet, item.getItemMeta().getPersistentDataContainer().get(keySet, PersistentDataType.STRING));
        }
        return list;
    }

    public boolean isProtected(ItemStack item) {
        if (getNBTValueByItemStack(item, "protected") != null)
            return Boolean.parseBoolean(getNBTValueByItemStack(item, "protected"));
        return false;
    }

    public String getAbility(ItemStack item) {
        return getNBTValueByItemStack(item, "ability");
    }

    public String getRarity(ItemStack item) {
        return getNBTValueByItemStack(item, "rarity");
    }

    public String getItemClass(ItemStack item) {
        return getNBTValueByItemStack(item, "class");
    }
}