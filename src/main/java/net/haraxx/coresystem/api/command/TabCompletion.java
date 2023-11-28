package net.haraxx.coresystem.api.command;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.function.Function;

/**
 * Describes all options for a tab-completion action while typing a command.
 * Will be used to generate results for bukkit's {@link TabCompleter#onTabComplete(CommandSender, Command, String, String[])}
 *
 * @author Juyas
 * @version 28.11.2023
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
     * All {@link OfflinePlayer}s as their names
     */
    TabCompletion OFFLINE_PLAYERS = () -> Arrays.stream( Bukkit.getOfflinePlayers() ).map( OfflinePlayer::getName ).toList();
    /**
     * All {@link Material}s as their enum names. e.g.: OAK_BOAT
     */
    TabCompletion MATERIAL = TabCompletion.of( Material.class );
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
    TabCompletion GAMEMODE = TabCompletion.of( GameMode.class );
    /**
     * All {@link GameMode}s as their numeric values
     */
    TabCompletion GAMEMODE_VALUES = () -> Arrays.stream( GameMode.values() ).map( GameMode::getValue ).map( String::valueOf ).toList();
    /**
     * All {@link World}s as their names
     */
    TabCompletion WORLDS = () -> Bukkit.getWorlds().stream().map( World::getName ).toList();
    /**
     * All {@link Plugin}s as their names
     */
    TabCompletion LOADED_PLUGINS = () -> Arrays.stream( Bukkit.getPluginManager().getPlugins() ).map( Plugin::getName ).toList();

    /**
     * Generate a completion for any enum
     *
     * @param enumClass the class of the enum
     *
     * @return a {@link TabCompletion} containing all constants of the given enum
     */
    static <T extends Enum<T>> TabCompletion of( Class<T> enumClass )
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
            options.addAll( Arrays.asList( additionalOptions ) );
            return options.stream().distinct().toList();
        };
    }

}
