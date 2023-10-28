package net.haraxx.coresystem.plugins.rpg.abilities;

import net.haraxx.coresystem.plugins.rpg.abilities.abilities.BSK_TOMAHAWK;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;

public class Abilities {

    private static final HashMap<String, AbilityRunner> abilitys = new HashMap<>();

    public static void addAbilities() {
        abilitys.put("BSK_TOMAHAWK", new BSK_TOMAHAWK());
    }

    public static HashMap<String, AbilityRunner> getAbilitys() {
        return abilitys;
    }
}
