package net.haraxx.coresystem.builder;

import net.haraxx.coresystem.CoreSystem;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    private Material material = Material.BARRIER;
    private int amount = 1;
    private short damage = 0;
    private MaterialData data;
    private int customModelData;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private List<ItemFlag> flags = new ArrayList<>();
    private String localizedName;
    private String itemClass;

    private boolean isProtected = false; // replace with NBT tag later on to remove invisible lore
    private boolean isGlowing = false;


    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.material = material;
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material);
        this.amount = amount;
        this.material = material;
    }

    public ItemBuilder(Material material, String displayName) {
        this.item = new ItemStack(material);
        this.material = material;
        this.displayName = displayName;
    }

    public ItemBuilder(Material material, int amount, String displayName) {
        this.item = new ItemStack(material, amount);
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        if (item.hasItemMeta())
            this.meta = item.getItemMeta();
        this.material = item.getType();
        this.amount = item.getAmount();
        this.damage = item.getDurability();
        this.enchantments = item.getEnchantments();
        if (item.hasItemMeta())
            this.displayName = item.getItemMeta().getDisplayName();
        if (item.hasItemMeta()) {
            this.lore = item.getItemMeta().getLore();
            this.isProtected = item.getItemMeta().getLore().contains("§0Protected");
        }
        if (item.hasItemMeta())
            this.flags.addAll(item.getItemMeta().getItemFlags());
    }

    public ItemBuilder setProtected() {
        this.isProtected = true;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack build() {
        item.setType(material);
        item.setAmount(amount);
        item.setDurability(damage);
        meta = item.getItemMeta();
        if (data != null)
            item.setData(data);
        if (enchantments.size() > 0)
            item.addUnsafeEnchantments(enchantments);
        if (displayName != null)
            meta.setDisplayName(displayName);
        if (isProtected) lore.add("§0Protected");
        if (isProtected) lore.add("§0" + itemClass);
        if (lore.size() > 0)
            meta.setLore(lore);
        if (customModelData != 0)
            meta.setCustomModelData(customModelData);
        if (flags.size() > 0) {
            for (ItemFlag f : flags) {
                meta.addItemFlags(f);
            }
        }
        meta.setLocalizedName(localizedName);
        item.setItemMeta(meta);
        return item;
    }


}
