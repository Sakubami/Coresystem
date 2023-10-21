package net.haraxx.coresystem.builder;

import java.util.ArrayList;

public class Chat {

    public static String format(String input) {
        return "§7» " + input;
    }

    public static String translate(String input) {
        return input.replace("&", "§");
    }

    public static ArrayList<String> translate(ArrayList<String> input) {
        ArrayList<String> newList = new ArrayList<>();
        for (String line : input) {
            String translated = line.replace("&", "§");
            newList.add(translated);
        }
        return newList;
    }
}
