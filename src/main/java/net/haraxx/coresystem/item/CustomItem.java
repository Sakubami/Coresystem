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
     * Creates an {@link ItemStack} containing all information of this {@link CustomItem}
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
    String getName();

    /**
     * The custom description of this {@link CustomItem}
     *
     * @return the description of this {@link CustomItem}. null, if this item does not have a description
     */
    @Nullable
    String getDescription();

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
    ItemRarity getRarity();

    /**
     * Whether the item has an ability. <br>
     * The result of this method should be equivalent to <code>{@link #getAbility()} == {@link ItemAbility#NONE}</code>
     *
     * @return true, if and only if this item has an ability. false otherwise
     */
    boolean hasAbility();

    /**
     * The item's ability. if this item does not have an ability, it should return {@link ItemAbility#NONE}
     *
     * @return the item's ability.
     *
     * @see ItemAbility
     */
    @Nonnull
    ItemAbility getAbility();

    /**
     * The {@link ItemClass} of this {@link CustomItem}. The itemClass restricts the usability by the players chosen rpg class.
     *
     * @return the item's {@link ItemClass}
     *
     * @see ItemClass
     */
    @Nonnull
    ItemClass getItemClass();

    /**
     * Checks the presence of an {@link ItemEnchantment}.
     *
     * @param type the {@link ItemEnchantmentType} of a potential {@link ItemEnchantment}
     *
     * @return true, if and only if this item has an {@link ItemEnchantment} of the given {@link ItemEnchantmentType}
     */
    boolean hasEnchantment( ItemEnchantmentType type );

    /**
     * Gets all enchantments on this item. <br>
     * This method returns {@link Collections#emptyList()} if there are no enchantments
     *
     * @return all {@link ItemEnchantment}
     */
    @Nonnull
    Set<ItemEnchantment> getEnchantments();

    /**
     * Checks the presence of an {@link ItemAttribute}. <br>
     * The result of this method should be equivalent to <code>{@link #getAttribute(ItemAttribute)} == 0</code>
     *
     * @param attribute the attribute to look for
     *
     * @return true, if and only if this item has the {@link ItemAttribute}
     */
    boolean hasAttribute( ItemAttribute attribute );

    /**
     * Gets the value associated with {@link ItemAttribute} of this item
     *
     * @param attribute the attribute to read
     *
     * @return the value of the attribute of this item. if the attribute is not set, it should return 0
     */
    double getAttribute( ItemAttribute attribute );

    /**
     * Gets all attributes associated with this item in a {@link HashMap}
     *
     * @return all attributes of this item
     */
    @Nonnull
    HashMap<ItemAttribute, Double> getAttributes();

}
