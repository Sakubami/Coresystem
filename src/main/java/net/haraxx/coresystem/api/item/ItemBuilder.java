package net.haraxx.coresystem.api.item;

import net.haraxx.coresystem.CoreSystem;
import net.haraxx.coresystem.builder.Chat;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

public class ItemBuilder
{

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
    private final NBT nbt = new NBT();

    private NamespacedKey key( String key )
    {
        return new NamespacedKey( CoreSystem.getInstance(), key );
    }

    public ItemBuilder( Material material )
    {
        this.item = new ItemStack( material );
        this.meta = item.hasItemMeta() && item.getItemMeta() != null ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta( item.getType() );
        this.material = material;
    }

    public ItemBuilder( Material material, int amount )
    {
        this.item = new ItemStack( material );
        this.meta = item.hasItemMeta() && item.getItemMeta() != null ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta( item.getType() );
        this.amount = amount;
        this.material = material;
    }

    public ItemBuilder( Material material, String displayName )
    {
        this.item = new ItemStack( material );
        this.meta = item.hasItemMeta() && item.getItemMeta() != null ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta( item.getType() );
        this.material = material;
        this.displayName = displayName;
    }

    public ItemBuilder( Material material, int amount, String displayName )
    {
        this.item = new ItemStack( material, amount );
        this.meta = item.hasItemMeta() && item.getItemMeta() != null ? item.getItemMeta() : Bukkit.getItemFactory().getItemMeta( item.getType() );
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
    }

    public ItemBuilder( ItemStack item )
    {
        this.item = item;
        if ( item.hasItemMeta() && item.getItemMeta() != null )
        {
            this.meta = item.getItemMeta();
            this.displayName = meta.getDisplayName();
            this.lore = meta.hasLore() && meta.getLore() != null ? meta.getLore() : new ArrayList<>();
            this.flags.addAll( item.getItemMeta().getItemFlags() );
            nbt.from( meta::getPersistentDataContainer );
        }
        else this.meta = Bukkit.getItemFactory().getItemMeta( item.getType() );
        this.material = item.getType();
        this.amount = item.getAmount();
        this.enchantments = item.getEnchantments();
    }

    public ItemBuilder( FileConfiguration cfg, String path )
    {
        this( cfg.getItemStack( path ) );
    }

    public static void toConfig( FileConfiguration cfg, String path, ItemBuilder builder )
    {
        cfg.set( path, builder.build() );
    }

    public <T> ItemBuilder nbt( String tag, Class<T> clazz, T value )
    {
        nbt.setTag( tag, clazz, value );
        return this;
    }

    public ItemBuilder nbt( String tag, String value )
    {
        nbt.setTag( tag, String.class, value );
        return this;
    }

    public ItemBuilder amount( int amount )
    {
        this.amount = amount;
        return this;
    }

    public ItemBuilder data( MaterialData data )
    {
        this.data = data;
        return this;
    }

    public ItemBuilder id( String id )
    {
        this.localizedName = id;
        return this;
    }

    public ItemBuilder material( Material material )
    {
        this.material = material;
        return this;
    }

    public ItemBuilder customModelData( int customModelData )
    {
        this.customModelData = customModelData;
        return this;
    }

    public ItemBuilder meta( ItemMeta meta )
    {
        this.meta = meta;
        return this;
    }

    public ItemBuilder enchant( Enchantment enchant, int level )
    {
        enchantments.put( enchant, level );
        return this;
    }

    public ItemBuilder enchant( Map<Enchantment, Integer> enchantments )
    {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder displayname( String displayname )
    {
        this.displayName = Chat.translate( displayname );
        return this;
    }

    public ItemBuilder setLore( List<String> lore )
    {
        this.lore = Chat.translate( lore );
        return this;
    }

    public ItemBuilder addToLore( String lore )
    {
        this.lore.add( Chat.translate( lore ) );
        return this;
    }

    public ItemBuilder editLore( int index, String lore )
    {
        this.lore.set( index, Chat.translate( lore ) );
        return this;
    }

    public ItemBuilder setGlowing()
    {
        this.isGlowing = true;
        return this;
    }

    public ItemStack build()
    {
        item.setType( material );
        item.setAmount( amount );
        if ( data != null )
            item.setData( data );
        if ( !enchantments.isEmpty() )
            item.addUnsafeEnchantments( enchantments );
        if ( displayName != null )
            meta.setDisplayName( displayName );
        if ( !lore.isEmpty() )
            meta.setLore( lore );
        if ( customModelData != 0 )
            meta.setCustomModelData( customModelData );
        if ( !flags.isEmpty() )
        {
            for ( ItemFlag f : flags )
            {
                meta.addItemFlags( f );
            }
        }
        if ( isGlowing )
        {
            meta.addEnchant( Enchantment.CHANNELING, 1, true );
            meta.addItemFlags( ItemFlag.HIDE_ENCHANTS );
        }
        meta.setLocalizedName( localizedName );
        nbt.apply( meta::getPersistentDataContainer );
        item.setItemMeta( meta );
        return item;
    }
}
