package net.haraxx.coresystem.api.services;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.sacredlabyrinth.phaed.simpleclans.*;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public EnumSet<PluginDependency> loadedDependencies()
    {
        return EnumSet.copyOf( Arrays.stream( PluginDependency.values() )
                .filter( PluginDependency::isLoaded ).collect( Collectors.toSet() ) );
    }

    @Override
    public Collection<String> addPermissions( Player player, String... permissions )
    {
        if ( permissions.length == 0 || !PluginDependency.LUCK_PERMS.isLoaded() ) return Collections.emptyList();
        UserManager manager = LuckPermsProvider.get().getUserManager();
        User user = manager.getUser( player.getUniqueId() );
        if ( user == null ) return Collections.emptyList();
        Set<String> notAdded = new HashSet<>();
        for ( String permission : permissions )
        {
            if ( !user.data().add( Node.builder( permission ).build() ).wasSuccessful() )
                notAdded.add( permission );
        }
        manager.saveUser( user );
        return notAdded;
    }

    @Override
    public Collection<String> removePermissions( Player player, String... permissions )
    {
        if ( permissions.length == 0 || !PluginDependency.LUCK_PERMS.isLoaded() ) return Collections.emptyList();
        UserManager manager = LuckPermsProvider.get().getUserManager();
        User user = manager.getUser( player.getUniqueId() );
        if ( user == null ) return Collections.emptyList();
        Set<String> notRemoved = new HashSet<>();
        for ( String permission : permissions )
        {
            if ( !user.data().remove( Node.builder( permission ).build() ).wasSuccessful() )
                notRemoved.add( permission );
        }
        manager.saveUser( user );
        return notRemoved;
    }

    @Override
    public String getClan( Player player )
    {
        if ( !PluginDependency.SIMPLE_CLANS.isLoaded() ) return null;
        ClanPlayer clanPlayer = SimpleClans.getInstance().getClanManager().getClanPlayer( player );
        if ( clanPlayer == null ) return null;
        Clan clan = clanPlayer.getClan();
        if ( clan == null ) return null;
        return clan.getName();
    }

    @Override
    public Collection<Player> getOnlineClanMembers( String clanTag )
    {
        if ( !PluginDependency.SIMPLE_CLANS.isLoaded() ) return Collections.emptyList();
        Clan clan = SimpleClans.getInstance().getClanManager().getClan( clanTag );
        if ( clan == null ) return Collections.emptyList();
        return clan.getOnlineMembers().stream().map( ClanPlayer::toPlayer ).toList();
    }

}
