package net.haraxx.coresystem.api.command;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Function;

/**
 * Describes all options for a tab-completion action while typing a command.
 * Will be used to generate results for bukkit's {@link TabCompleter#onTabComplete(CommandSender, Command, String, String[])}
 *
 * @author Juyas
 * @version 24.11.2023
 * @see ICommand
 * @see BukkitCommand
 * @see CommandGroup
 * @since 12.11.2023
 */
public interface TabCompletion
{

    /**
     * Nothing to complete
     */
    TabCompletion NONE = Collections::emptyList;
    /**
     * All online {@link Player}s as their names
     */
    TabCompletion PLAYERS = () -> Bukkit.getOnlinePlayers().stream().map( Player::getName ).toList();
    /**
     * All {@link Material}s as their enum names. e.g.: OAK_BOAT
     */
    TabCompletion MATERIAL = () -> Arrays.stream( Material.values() ).map( Material::name ).toList();
    /**
     * All {@link Material}s as their minecraft namespaced tags. e.g.: minecraft:oak_boat
     */
    TabCompletion MATERIAL_KEYS = () -> Arrays.stream( Material.values() ).map( mat -> mat.getKey().toString() ).toList();
    /**
     * All item {@link Enchantment}s as their names. e.g.: ARROW_FIRE
     */
    TabCompletion ENCHANTMENTS = () -> Arrays.stream( Enchantment.values() ).map( Enchantment::getName ).toList();
    /**
     * All item {@link Enchantment}s as their minecraft namespaced tags. e.g.: minecraft:flame
     */
    TabCompletion ENCHANTMENT_KEYS = () -> Arrays.stream( Enchantment.values() ).map( ench -> ench.getKey().toString() ).toList();
    /**
     * All {@link GameMode}s as their names
     */
    TabCompletion GAMEMODE = () -> Arrays.stream( GameMode.values() ).map( GameMode::name ).toList();
    /**
     * All {@link GameMode}s as their numeric values
     */
    TabCompletion GAMEMODE_VALUES = () -> Arrays.stream( GameMode.values() ).map( GameMode::getValue ).map( String::valueOf ).toList();

    /**
     * Generate a completion for any enum
     *
     * @param enumClass the class of the enum
     *
     * @return a {@link TabCompletion} containing all constants of the given enum
     */
    static TabCompletion of( Class<Enum<?>> enumClass )
    {
        return () -> Arrays.stream( enumClass.getEnumConstants() ).map( Enum::name ).toList();
    }

    /**
     * Generate a completion for a collection of objects
     *
     * @param options   all option values
     * @param extractor the method to extract a descriptive string from any given value
     * @param <T>       the type to ensure the function to work on the list of given objects
     *
     * @return a {@link TabCompletion} containing values representing all given objects
     */
    static <T> TabCompletion of( Collection<T> options, Function<T, String> extractor )
    {
        return () -> options.stream().map( extractor ).distinct().toList();
    }

    /**
     * Should contain all options available to complete a specific argument
     *
     * @return all completion options
     */
    List<String> tabOptions();

    /**
     * Combine two completion lists together.
     * Mathematically one might describe it as the union of both sets.
     * There is no check for duplicates though.
     *
     * @param completion the completion to add
     *
     * @return a completion combining the existing options with the new ones
     */
    default TabCompletion concat( TabCompletion completion )
    {
        return () -> {
            List<String> options = new ArrayList<>( this.tabOptions() );
            options.addAll( completion.tabOptions() );
            return options;
        };
    }

    /**
     * Combine the existing completion with a list of strings
     *
     * @param additionalOptions the options to add
     *
     * @return a completion combining the existing options with the new ones
     */
    default TabCompletion include( String... additionalOptions )
    {
        return () ->
        {
            List<String> options = new ArrayList<>( this.tabOptions() );
            options.addAll( Arrays.stream( additionalOptions ).distinct().toList() );
            return options;
        };
    }

}
