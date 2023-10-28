package net.haraxx.coresystem.plugins.rpg.item;

import net.haraxx.coresystem.CoreSystem;
import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.plugins.rpg.abilities.Abilities;
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
    private short durability = 0;
    private MaterialData data;
    private int customModelData;
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private String displayName;
    private List<String> lore = new ArrayList<>();
    private List<ItemFlag> flags = new ArrayList<>();
    private String localizedName;
    private ItemMeta NBTmeta;

    private String itemClass; //TODO replace with NBT tag later on to remove invisible lore
    private String ability; //TODO replace with NBT tag later on to remove invisible lore
    private boolean isProtected = false; //TODO replace with NBT tag later on to remove invisible lore

    private boolean isGlowing = false;

    private final NBTapi nbtData = new NBTapi();
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
        this.durability = item.getDurability();
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
            nbtData.extractNBTData(meta);
    }

    public ItemBuilder(FileConfiguration cfg, String path) {
        this(cfg.getItemStack(path));
    }

    public static void toConfig(FileConfiguration cfg, String path, ItemBuilder builder) {
        cfg.set(path, builder.build());
    }

    public ItemBuilder addNBTTag(String key, String value) {
        nbtData.addNBTTag(key(key), value);
        return this;
    }

    public ItemBuilder addNBTTagList(HashMap<String, String> value) {
        HashMap <NamespacedKey, String> list = new HashMap<>();
        for (String str: value.keySet()) {
            list.put(key(str), value.get(str));
        }
        nbtData.addAllNBTTagList(list);
        return this;
    }


    public ItemBuilder setProtected(boolean value) {
        nbtData.addNBTTag(key("protected"), String.valueOf(value));
        return this;
    }

    public ItemBuilder ability(String ability) {
        nbtData.addNBTTag(key("ability"), ability);
        return this;
    }

    public ItemBuilder itemClass(String itemClass) {
        List<String> classes = Arrays.asList("BERSERK", "MAGE", "DEFAULT");
        if (classes.contains(itemClass.toUpperCase())) {
            nbtData.addNBTTag(key("class"), itemClass);
        } else nbtData.addNBTTag(key("class"), "DEFAULT");
        return this;
    }

    public ItemBuilder rarity(String rarity) {
        List<String> rarities = Arrays.asList("DIVINE", "MYTHIC", "LEGENDARY", "EPIC", "RARE", "UNCOMMON", "COMMON");
        if (rarities.contains(rarity.toUpperCase())) {
            nbtData.addNBTTag(key("rarity"), rarity);
        } else nbtData.addNBTTag(key("rarity"), "COMMON");
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

    public ItemBuilder durability(short damage) {
        this.durability = damage;
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
        item.setDurability(durability);
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
        meta.setLocalizedName(localizedName);
        meta = nbtData.parseAllNBTTags(meta);
        item.setItemMeta(meta);
        return item;
    }
}
