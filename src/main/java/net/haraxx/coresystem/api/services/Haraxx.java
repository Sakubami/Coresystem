package net.haraxx.coresystem.api.services;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Juyas
 * @version 25.11.2023
 * @since 25.11.2023
 */
public interface Haraxx
{

    String rawTextPrefix();

    String coloredTextPrefix();

    String translateColors( String text );

    List<String> translateColors( List<String> text );

    List<String> translateColors( String... text );

    boolean isPlayerVerified( Player player );

    boolean isPlayerSmurf( Player player );

    void setPlayerVerified( Player player, boolean verify );

    void setPlayerSmurf( Player player, boolean smurf );

    PlayerRole getPlayerRole( Player player );

    void setPlayerRole( Player player, PlayerRole role );

    int getPlayerRank( Player player );

    void setPlayerRank( Player player, int rank );

    Location getWorldSpawn( World world );

}
