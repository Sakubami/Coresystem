package net.haraxx.coresystem.builder;

import org.bukkit.ChatColor;

import java.util.List;

public class Chat {

    public static String format(String input) {
        return "§7» " + input;
    }

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> translate( List<String> input) {
        for (String line : input) {
            ChatColor.translateAlternateColorCodes('&', line);
        }
        return input;
    }
}
