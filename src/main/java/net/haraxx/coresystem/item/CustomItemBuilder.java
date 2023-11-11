package net.haraxx.coresystem.item;

import net.haraxx.coresystem.builder.item.ItemBuilder;
import net.haraxx.coresystem.builder.item.NBT;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Juyas
 * @version 11.11.2023
 * @since 11.11.2023
 */
public class CustomItemBuilder
{

    private static final String TEXT_ABILITY = "Fähigkeit:";
    private static final String TEXT_LEVEL = "Level";
    private static final String TEXT_ABILITY_HINT = "RMT";

    private static final int TEXT_WIDTH_LIMIT = 30;

    private static final NumberFormat numberFormatter = NumberFormat.getInstance();

    static
    {
        numberFormatter.setRoundingMode( RoundingMode.DOWN );
        numberFormatter.setMinimumIntegerDigits( 1 );
        numberFormatter.setMaximumFractionDigits( 1 );
    }

    // NBT constants
    private static final String NBT_PREFIX = "custom_item:";

    private static final String NBT_NAME = NBT_PREFIX + "name";
    private static final String NBT_ATTRIBUTES = NBT_PREFIX + "attributes";
    private static final String NBT_PROTECTION = NBT_PREFIX + "protect";
    private static final String NBT_ENCHANTMENTS = NBT_PREFIX + "enchantments";
    private static final String NBT_PASSIVES = NBT_PREFIX + "passives";
    private static final String NBT_ABILITY = NBT_PREFIX + "primary-ability";
    private static final String NBT_DESCRIPTION = NBT_PREFIX + "description";
    private static final String NBT_CLASS = NBT_PREFIX + "class";
    private static final String NBT_CLASS_LEVEL_REQUIREMENT = NBT_PREFIX + "class-level-requirement";
    private static final String NBT_RARITY = NBT_PREFIX + "rarity";

    //color coding
    private static final ChatColor CL_ATTRIBUTE_NAME = ChatColor.GRAY;
    private static final ChatColor CL_OFFENSIVE_ATTRIBUTE = ChatColor.RED;
    private static final ChatColor CL_STAT_ATTRIBUTE = ChatColor.GREEN;
    private static final ChatColor CL_ENCHANTMENT = ChatColor.BLUE;
    private static final ChatColor CL_ABILITY = ChatColor.GOLD;
    private static final ChatColor CL_ABILITY_HINT = ChatColor.YELLOW;
    private static final ChatColor CL_DESCRIPTION = ChatColor.GRAY;
    private static final ChatColor CL_CLASS_RESTRICTION = ChatColor.DARK_RED;
    private static final ChatColor CL_ITEM_TYPE = ChatColor.GOLD;


    private String itemName;
    private String description;
    private boolean itemProtection;
    private ItemRarity rarity;
    private ItemClass itemClass;
    private int minimumRequiredClassLevel;
    private ItemAbility ability;
    private Map<ItemEnchantmentType, Integer> enchantments;
    private ItemEnchantmentNumberMode itemEnchantmentNumberMode;
    private Map<ItemAttribute, Double> attributes;
    private Map<ItemPassiveType, Double> passives;

    public CustomItemBuilder()
    {
        this.rarity = ItemRarity.COMMON;
        this.ability = ItemAbility.NONE;
        this.itemClass = ItemClass.DEFAULT;
        this.itemEnchantmentNumberMode = ItemEnchantmentNumberMode.ROMAN;
        this.minimumRequiredClassLevel = 0;
        this.itemProtection = false;
        this.attributes = new HashMap<>();
        this.enchantments = new HashMap<>();
        this.passives = new HashMap<>();
    }

    public CustomItemBuilder( String itemName )
    {
        this();
        this.itemName = itemName;
    }

    public CustomItemBuilder name( String name )
    {
        this.itemName = name;
        return this;
    }

    public CustomItemBuilder desc( String description )
    {
        this.description = description;
        return this;
    }

    public CustomItemBuilder protect( boolean protection )
    {
        this.itemProtection = protection;
        return this;
    }

    public CustomItemBuilder rarity( ItemRarity rarity )
    {
        this.rarity = rarity;
        return this;
    }

    public CustomItemBuilder requireClass( ItemClass itemClass )
    {
        this.itemClass = itemClass;
        return this;
    }

    public CustomItemBuilder requireClassLevel( int level )
    {
        this.minimumRequiredClassLevel = level;
        return this;
    }

    public CustomItemBuilder ability( ItemAbility ability )
    {
        this.ability = ability;
        return this;
    }

    public CustomItemBuilder enchant( ItemEnchantmentType type, int level )
    {
        this.enchantments.put( type, level );
        return this;
    }

    public CustomItemBuilder disenchant( ItemEnchantmentType type )
    {
        this.enchantments.remove( type );
        return this;
    }

    public CustomItemBuilder enchantments( HashMap<ItemEnchantmentType, Integer> enchantments )
    {
        this.enchantments = enchantments;
        return this;
    }

    public CustomItemBuilder enchantmentNumberMode( ItemEnchantmentNumberMode mode )
    {
        this.itemEnchantmentNumberMode = mode;
        return this;
    }

    public CustomItemBuilder passive( ItemPassiveType type, double value )
    {
        this.passives.put( type, value );
        return this;
    }

    public CustomItemBuilder addToPassive( ItemPassiveType type, double modValue )
    {
        this.passives.put( type, this.passives.getOrDefault( type, 0.0 ) + modValue );
        return this;
    }

    public CustomItemBuilder passives( HashMap<ItemPassiveType, Double> passives )
    {
        this.passives = passives;
        return this;
    }

    public CustomItemBuilder attribute( ItemAttribute attribute, double newValue )
    {
        this.attributes.put( attribute, newValue );
        return this;
    }

    public CustomItemBuilder addToAttribute( ItemAttribute attribute, double modValue )
    {
        this.attributes.put( attribute, this.attributes.getOrDefault( attribute, 0.0 ) + modValue );
        return this;
    }

    public CustomItemBuilder attributes( HashMap<ItemAttribute, Double> attributes )
    {
        this.attributes = attributes;
        return this;
    }

    public CustomItemBuilder remAttribute( ItemAttribute attribute )
    {
        this.attributes.remove( attribute );
        return this;
    }

    public CustomItem build( Material material )
    {
        LoreBuilder loreBuilder = new LoreBuilder( TEXT_WIDTH_LIMIT );
        //offensive attributes first
        loreBuilder.add( attributes.entrySet().stream().filter( entry -> !entry.getKey().isStat() && entry.getValue() != 0 ),
                ( key, value ) -> CL_ATTRIBUTE_NAME + key.getDisplayName() + ": "
                        + CL_OFFENSIVE_ATTRIBUTE + ( value > 0 ? "+" + numberFormatter.format( value ) : numberFormatter.format( value ) ) );
        loreBuilder.optionalBreak();
        //stat attributes last
        loreBuilder.add( attributes.entrySet().stream().filter( entry -> entry.getKey().isStat() && entry.getValue() != 0 ),
                ( key, value ) -> CL_ATTRIBUTE_NAME + key.getDisplayName() + ": "
                        + CL_STAT_ATTRIBUTE + ( value > 0 ? "+" + numberFormatter.format( value ) : numberFormatter.format( value ) ) );
        loreBuilder.optionalBreak();
        //add enchantments
        loreBuilder.addElements( enchantments, ( type, level ) -> ( type.getOverrideColor() == null ? CL_ENCHANTMENT : type.getOverrideColor() )
                + type.getDisplayName() + " " + itemEnchantmentNumberMode.get( level ) );
        //add passives
        loreBuilder.add( passives.entrySet().stream().map( entry -> String.format( entry.getKey().getDescription(), numberFormatter.format( entry.getValue() ) ) ).collect( Collectors.joining( " " ) ) );
        //display ability if there is one
        if ( ability != ItemAbility.NONE )
            loreBuilder.add( CL_ABILITY + TEXT_ABILITY + " " + ability.getDisplayName() + " " + CL_ABILITY_HINT + TEXT_ABILITY_HINT );
        //add description lore
        loreBuilder.add( description, true, true );
        //class restriction info and level requirement
        loreBuilder.add( CL_CLASS_RESTRICTION + itemClass.getDisplayName() + ( minimumRequiredClassLevel > 0 ? "(" + TEXT_LEVEL + " " + minimumRequiredClassLevel + ")" : "" ) );
        //item rarity
        loreBuilder.add( rarity.getColor() + rarity.name() );
        Supplier<ItemStack> itemStackSupplier = getItemStackSupplier( material, loreBuilder );

        //extract sets
        Set<ItemEnchantment> itemEnchantments = enchantments.entrySet().stream().map( entry -> new ItemEnchantmentBase( entry.getKey(), entry.getValue() ) ).collect( Collectors.toSet() );
        Set<ItemPassive> itemPassives = passives.entrySet().stream().map( entry -> new ItemPassiveBase( entry.getKey(), entry.getValue() ) ).collect( Collectors.toSet() );
        return new CustomItemBase( itemName, description, itemProtection, rarity, ability, itemClass, minimumRequiredClassLevel,
                EnumSet.copyOf( enchantments.keySet() ), itemEnchantments, EnumSet.copyOf( passives.keySet() ), itemPassives, Collections.unmodifiableMap( attributes ), itemStackSupplier );
    }

    @NotNull
    private Supplier<ItemStack> getItemStackSupplier( Material material, LoreBuilder loreBuilder )
    {
        List<String> lore = loreBuilder.build();
        return () ->
        {
            //create builder
            ItemBuilder itemBuilder = new ItemBuilder( material )
                    .displayname( rarity.getColor() + itemName )
                    .setLore( lore )
                    .nbt( NBT_NAME, itemName )
                    .nbt( NBT_ABILITY, ability.name() )
                    .nbt( NBT_CLASS, itemClass.name() )
                    .nbt( NBT_RARITY, rarity.name() )
                    .nbt( NBT_PROTECTION, Boolean.toString( itemProtection ) )
                    .nbt( NBT_CLASS_LEVEL_REQUIREMENT, Integer.class, minimumRequiredClassLevel );
            if ( description != null )
                itemBuilder.nbt( NBT_DESCRIPTION, description );
            ItemStack stack = itemBuilder.build();
            //post build edits - meta should be non-null, since it's an already built item
            ItemMeta meta = stack.getItemMeta();
            PersistentDataContainer pdc = Objects.requireNonNull( meta ).getPersistentDataContainer();
            PersistentDataContainer enchantsContainer = pdc.getAdapterContext().newPersistentDataContainer();
            PersistentDataContainer passivesContainer = pdc.getAdapterContext().newPersistentDataContainer();
            PersistentDataContainer attributesContainer = pdc.getAdapterContext().newPersistentDataContainer();
            enchantments.forEach( (type, level) -> enchantsContainer.set( NBT.key( type.name() ), PersistentDataType.INTEGER, level ) );
            passives.forEach( (passive, value) -> passivesContainer.set( NBT.key( passive.name() ), PersistentDataType.DOUBLE, value ) );
            attributes.forEach( (attr, value) -> attributesContainer.set( NBT.key( attr.name() ), PersistentDataType.DOUBLE, value ) );
            pdc.set( NBT.key( NBT_ENCHANTMENTS ), PersistentDataType.TAG_CONTAINER, enchantsContainer );
            pdc.set( NBT.key( NBT_PASSIVES ), PersistentDataType.TAG_CONTAINER, passivesContainer );
            pdc.set( NBT.key( NBT_ATTRIBUTES ), PersistentDataType.TAG_CONTAINER, attributesContainer );
            stack.setItemMeta( meta );
            return stack;
        };
    }

    private static class LoreBuilder
    {

        private final List<String> lore = new ArrayList<>();

        private final int maxTextWidth;

        public LoreBuilder( int maxTextWidth )
        {
            this.maxTextWidth = maxTextWidth;
        }

        public List<String> build()
        {
            return lore;
        }

        public void add( String line )
        {
            lore.add( line );
        }

        public void optionalBreak()
        {
            if ( !lore.isEmpty() && !lore.get( lore.size() - 1 ).isEmpty() ) add( "" );
        }

        public <K, V> void add( Stream<Map.Entry<K, V>> entryStream, BiFunction<K, V, String> convert )
        {
            entryStream.map( entry -> convert.apply( entry.getKey(), entry.getValue() ) ).forEach( this::add );
        }

        public <K, V> void addElements( Map<K, V> map, BiFunction<K, V, String> convert )
        {
            add( map.entrySet().stream().map( entry -> convert.apply( entry.getKey(), entry.getValue() ) ).toList(), maxTextWidth );
        }

        public void add( List<String> elements, int maxTextWidth )
        {
            StringBuilder builder = new StringBuilder();
            for ( String elem : elements )
            {
                //split line if next word would go past the set limit
                if ( builder.length() + elem.length() > maxTextWidth )
                {
                    add( builder.substring( 2 ) );
                    builder = new StringBuilder();
                }
                else builder.append( ", " ).append( elem );
            }
            //add remaining text
            if ( !builder.isEmpty() )
                add( builder.substring( 2 ) );
        }

        public void add( String line, boolean wordBreak, boolean keepColors )
        {
            //split by words
            if ( wordBreak )
            {
                String[] split = line.split( "" );
                StringBuilder builder = new StringBuilder();
                String last;
                for ( String elem : split )
                {
                    //split line if next word would go past the set limit
                    if ( builder.length() + elem.length() > maxTextWidth )
                    {
                        add( last = builder.substring( 1 ) );
                        builder = new StringBuilder();
                        if ( keepColors ) builder.append( ChatColor.getLastColors( last ) );
                    }
                    else builder.append( " " ).append( elem.length() );
                }
                //add remaining text
                if ( !builder.isEmpty() )
                    add( builder.substring( 1 ) );
                return;
            }
            //split hard
            while ( line.length() > maxTextWidth )
            {
                int breakPoint = maxTextWidth;
                //adjust possible in-color cuts
                if ( line.charAt( breakPoint - 1 ) == ChatColor.COLOR_CHAR ) breakPoint = breakPoint - 1;
                String substring = line.substring( 0, breakPoint );
                line = ( keepColors ? ChatColor.getLastColors( substring ) : "" ) + line.substring( breakPoint );
            }
            add( line );
        }

    }

    private record ItemPassiveBase(ItemPassiveType type, double value) implements ItemPassive {}

    private record ItemEnchantmentBase(ItemEnchantmentType type, int level) implements ItemEnchantment {}

    private record CustomItemBase(String name, String description, boolean isProtected, ItemRarity rarity,
                                  ItemAbility ability, ItemClass itemClass, int classLevelRequirement,
                                  EnumSet<ItemEnchantmentType> enchantmentTypes, Set<ItemEnchantment> enchantments,
                                  EnumSet<ItemPassiveType> passiveTypes, Set<ItemPassive> passives,
                                  Map<ItemAttribute, Double> attributes,
                                  Supplier<ItemStack> itemStackSupplier) implements CustomItem
    {

        @NotNull
        @Override
        public ItemStack getItemStack()
        {
            return itemStackSupplier.get();
        }

        @Override
        public boolean hasEnchantment( ItemEnchantmentType type )
        {
            return enchantmentTypes.contains( type );
        }

        @Override
        public boolean hasPassive( ItemPassiveType type )
        {
            return passiveTypes.contains( type );
        }
    }

}
