package net.haraxx.coresystem.builder.item;

import net.haraxx.coresystem.CoreSystem;
import net.haraxx.coresystem.builder.Chat;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

public class ItemBuilder {
    private ItemStack item;
    private ItemMeta meta;
    private Material material = Material.BARRIER;
    private int amount = 1;
    private MaterialData data;
    private int customModelData;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private List<ItemFlag> flags = new ArrayList<>();
    private String localizedName;
    private boolean isGlowing = false;

    private final NBTapi nbtData = new NBTapi();
    private final NBTapi.Unsafe unsafe = nbtData.getUnsafe();
    private NamespacedKey key(String key) { return new NamespacedKey(CoreSystem.getInstance(), key); }

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
        this.enchantments = item.getEnchantments();
        if (item.hasItemMeta())
            this.displayName = item.getItemMeta().getDisplayName();
        if (item.hasItemMeta()) {
            if (item.getItemMeta().getLore() != null) {
                this.lore = item.getItemMeta().getLore();
            }
        }
        if (item.hasItemMeta())
            this.flags.addAll(item.getItemMeta().getItemFlags());
        if (item.hasItemMeta())
            unsafe.readNBTData(meta);
    }

    public ItemBuilder(FileConfiguration cfg, String path) {
        this(cfg.getItemStack(path));
    }

    public static void toConfig(FileConfiguration cfg, String path, ItemBuilder builder) {
        cfg.set(path, builder.build());
    }

    public ItemBuilder setProtected() {
        unsafe.addNBTTag(key("protected"), "true");
        return this;
    }

    public ItemBuilder ability(String ability) {
        unsafe.addNBTTag(key("ability"), ability);
        return this;
    }

    public ItemBuilder damage(short damage) {
        unsafe.addNBTTag(key("damage"), String.valueOf(damage));
        return this;
    }

    public ItemBuilder damageBonus(short damage) {
        unsafe.addNBTTag(key("damagebonus"), String.valueOf(damage));
        return this;
    }

    public ItemBuilder abilityBonus(short damage) {
        unsafe.addNBTTag(key("abilitybonus"), String.valueOf(damage));
        return this;
    }

    public ItemBuilder itemClass(String itemClass) {
        List<String> classes = Arrays.asList("BERSERK", "MAGE", "DEFAULT");
        if (classes.contains(itemClass.toUpperCase())) {
            unsafe.addNBTTag(key("class"), itemClass);
        } else unsafe.addNBTTag(key("class"), "DEFAULT");
        return this;
    }

    public ItemBuilder rarity(String rarity) {
        List<String> rarities = Arrays.asList("DIVINE", "MYTHIC", "LEGENDARY", "EPIC", "RARE", "UNCOMMON", "COMMON");
        if (rarities.contains(rarity.toUpperCase())) {
            unsafe.addNBTTag(key("rarity"), rarity);
        } else unsafe.addNBTTag(key("rarity"), "COMMON");
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder data(MaterialData data) {
        this.data = data;
        return this;
    }

    public ItemBuilder id(String id) {
        this.localizedName = id;
        return this;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder customModelData(int customModelData) {
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder meta(ItemMeta meta) {
        this.meta = meta;
        return this;
    }

    public ItemBuilder enchant(Enchantment enchant, int level) {
        enchantments.put(enchant, level);
        return this;
    }

    public ItemBuilder enchant(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder displayname(String displayname) {
        this.displayName = Chat.translate(displayname);
        return this;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        this.lore = Chat.translate(lore);
        return this;
    }

    public ItemBuilder addToLore(String lore) {
        this.lore.add(Chat.translate(lore));
        return this;
    }

    public ItemBuilder setGlowing() {
        this.isGlowing = true;
        return this;
    }

    public ItemStack build() {
        item.setType(material);
        item.setAmount(amount);
        meta = item.getItemMeta();
        if (data != null)
            item.setData(data);
        if (enchantments.size() > 0)
            item.addUnsafeEnchantments(enchantments);
        if (displayName != null)
            meta.setDisplayName(displayName);
        if (lore.size() > 0)
            meta.setLore(lore);
        if (customModelData != 0)
            meta.setCustomModelData(customModelData);
        if (flags.size() > 0) {
            for (ItemFlag f : flags) {
                meta.addItemFlags(f);
            }
        }
        if (isGlowing) {
            meta.addEnchant(Enchantment.CHANNELING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setLocalizedName(localizedName);
        meta = unsafe.writeNBTData(meta);
        item.setItemMeta(meta);
        return item;
    }
}
