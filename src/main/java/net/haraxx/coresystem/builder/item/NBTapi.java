package net.haraxx.coresystem.builder.item;

import net.haraxx.coresystem.CoreSystem;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class NBTapi {

    private NamespacedKey key(String key) { return new NamespacedKey(CoreSystem.getInstance(), key); }

    public String getNBTTag(ItemStack item, String key) {
        return item.getItemMeta().getPersistentDataContainer().get(key(key), PersistentDataType.STRING);
    }

    public HashMap<NamespacedKey, String> getNBTTags(ItemStack item) {
        HashMap<NamespacedKey, String> list = new HashMap<>();
        for (NamespacedKey key: item.getItemMeta().getPersistentDataContainer().getKeys()) {
            list.put(key, item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
        }
        return list;
    }

    // do not use outside of itembuilder because it will probably not work properly

    public Unsafe getUnsafe() {
        return new Unsafe();
    }

    protected class Unsafe {
        private final HashMap<NamespacedKey, String> list = new HashMap<>();

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
    }
}