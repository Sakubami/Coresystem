package net.haraxx.coresystem.api.services;

import org.bukkit.permissions.Permissible;

/**
 * @author Juyas
 * @version 25.11.2023
 * @since 25.11.2023
 */
public enum PlayerRole
{
    UNVERIFIED("group.unverified"),
    PLAYER("group.default"),
    BUILDER("group.builder"), //TODO check if this permission is real
    SUPPORTER("group.supporter"),
    MODERATOR("group.moderator"), //TODO check if this permission is real
    DEVELOPER("group.developer"),
    ADMIN("group.admin"); //TODO check if this permission is real
    private final String permission;

    PlayerRole( String permission )
    {
        this.permission = permission;
    }

    public String getPermission()
    {
        return permission;
    }

    public boolean has( Permissible permissible )
    {
        return permissible.hasPermission( permission );
    }

}
