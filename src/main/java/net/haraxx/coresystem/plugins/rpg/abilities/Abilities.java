package net.haraxx.coresystem.plugins.rpg.abilities;

import net.haraxx.coresystem.plugins.rpg.abilities.abilities.BSK_TOMAHAWK;
import net.haraxx.coresystem.plugins.rpg.abilities.abilities.RDM_RESOLUTION;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;

public class Abilities {

    private static final HashMap<String, AbilityRunner> abilitys = new HashMap<>();

    public static void initiateAbilitys() {
        abilitys.put("BSK_TOMAHAWK", new BSK_TOMAHAWK());
        abilitys.put("RDM_RESOLUTION", new RDM_RESOLUTION());
    }

    public static HashMap<String, AbilityRunner> getAbilitys() {
        return abilitys;
    }
}
