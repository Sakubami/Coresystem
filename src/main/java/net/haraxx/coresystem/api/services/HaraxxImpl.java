package net.haraxx.coresystem.api.services;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @author Juyas
 * @version 25.11.2023
 * @since 25.11.2023
 */
public class HaraxxImpl implements Haraxx
{

    @Override
    public String rawTextPrefix()
    {
        return "» ";
    }

    @Override
    public String coloredTextPrefix()
    {
        return "§7» ";
    }

    @Override
    public String translateColors( String text )
    {
        return ChatColor.translateAlternateColorCodes( '&', text );
    }

    @Override
    public List<String> translateColors( List<String> text )
    {
        return text.stream().map( this::translateColors ).toList();
    }

    @Override
    public List<String> translateColors( String... text )
    {
        return translateColors( Arrays.asList( text ) );
    }

    @Override
    public boolean isPlayerVerified( Player player )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }

    @Override
    public boolean isPlayerSmurf( Player player )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }

    @Override
    public void setPlayerVerified( Player player, boolean verify )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }

    @Override
    public void setPlayerSmurf( Player player, boolean smurf )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }

    @Override
    public PlayerRole getPlayerRole( Player player )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }

    @Override
    public void setPlayerRole( Player player, PlayerRole role )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }

    @Override
    public int getPlayerRank( Player player )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }

    @Override
    public void setPlayerRank( Player player, int rank )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }

    @Override
    public Location getWorldSpawn( World world )
    {
        //TODO implement
        throw new UnsupportedOperationException( "not yet implemented" );
    }
}
