package net.haraxx.coresystem.item;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Juyas
 * @version 10.11.2023
 * @since 10.11.2023
 */
public interface CustomItem
{

    /**
     * Creates a new {@link ItemStack} containing all information of this {@link CustomItem}
     *
     * @return a bukkit {@link ItemStack}
     *
     * @see ItemStack
     */
    @Nonnull
    ItemStack getItemStack();

    /**
     * The custom name of this {@link CustomItem}
     *
     * @return the name of this {@link CustomItem}
     */
    @Nonnull
    String name();

    /**
     * The custom description of this {@link CustomItem}
     *
     * @return the description of this {@link CustomItem}. null, if this item does not have a description
     */
    @Nullable
    String description();

    /**
     * The protection should restrain the item from being removed from its slots
     *
     * @return true, if and only if this item is protected. false otherwise
     */
    boolean isProtected();

    /**
     * The {@link ItemRarity} of this {@link CustomItem}
     *
     * @return the rarity of this item
     *
     * @see ItemRarity
     */
    @Nonnull
    ItemRarity rarity();

    /**
     * Whether the item has an ability. <br>
     * The result of this method should be equivalent to <code>{@link #ability()} == {@link ItemAbility#NONE}</code>
     *
     * @return true, if and only if this item has an ability. false otherwise
     */
    default boolean hasAbility()
    {
        return ability() == ItemAbility.NONE;
    }

    /**
     * The item's ability. if this item does not have an ability, it should return {@link ItemAbility#NONE}
     *
     * @return the item's ability.
     *
     * @see ItemAbility
     */
    @Nonnull
    ItemAbility ability();

    /**
     * The {@link ItemClass} of this {@link CustomItem}. The itemClass restricts the usability by the players chosen rpg class.
     *
     * @return the item's {@link ItemClass}
     *
     * @see ItemClass
     */
    @Nonnull
    ItemClass itemClass();

    /**
     * The minimum required class level to use this {@link CustomItem}. <br>
     * A minimum required level of 0 indicates, that there is no requirement.
     *
     * @return the minimum required class level; 0 or greater
     */
    int classLevelRequirement();

    /**
     * Checks the presence of an {@link ItemEnchantment}.
     *
     * @param type the {@link ItemEnchantmentType} of a potential {@link ItemEnchantment}
     *
     * @return true, if and only if this item has an {@link ItemEnchantment} of the given {@link ItemEnchantmentType}
     */
    boolean hasEnchantment( ItemEnchantmentType type );

    /**
     * Gets all {@link ItemEnchantment}s on this {@link CustomItem}. <br>
     * This method returns {@link Collections#emptyList()} if there are no enchantments
     *
     * @return all {@link ItemEnchantment}
     */
    @Nonnull
    Set<ItemEnchantment> enchantments();

    /**
     * Checks the presence of an {@link ItemPassive}.
     *
     * @param type the {@link ItemPassiveType} of a potential {@link ItemPassive}
     *
     * @return true, if and only if this item has an {@link ItemPassive} of the given {@link ItemPassiveType}
     */
    boolean hasPassive( ItemPassiveType type );

    /**
     * Gets all {@link ItemPassive}s of this {@link CustomItem}. <br>
     * This method returns {@link Collections#emptyList()} if there are no passives
     *
     * @return all {@link ItemPassive}s
     */
    Set<ItemPassive> passives();

    /**
     * Checks the presence of an {@link ItemAttribute}. <br>
     * The result of this method should be equivalent to <code>{@link #getAttribute(ItemAttribute)} == 0</code>
     *
     * @param attribute the attribute to look for
     *
     * @return true, if and only if this item has the {@link ItemAttribute}
     */
    default boolean hasAttribute( ItemAttribute attribute )
    {
        return attributes().containsKey( attribute );
    }

    /**
     * Gets the value associated with {@link ItemAttribute} of this item
     *
     * @param attribute the attribute to read
     *
     * @return the value of the attribute of this item. if the attribute is not set, it should return 0
     */
    default double getAttribute( ItemAttribute attribute )
    {
        return attributes().getOrDefault( attribute, 0.0 );
    }

    /**
     * Gets all attributes associated with this item in a {@link HashMap}
     *
     * @return all attributes of this item
     */
    @Nonnull
    Map<ItemAttribute, Double> attributes();

}
