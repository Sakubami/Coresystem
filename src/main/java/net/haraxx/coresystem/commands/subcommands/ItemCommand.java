package net.haraxx.coresystem.commands.subcommands;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.builder.item.NBTapi;
import net.haraxx.coresystem.commands.CommandRunner;
import net.haraxx.coresystem.builder.item.ItemBuilder;
import net.haraxx.coresystem.plugins.rpg.player.RPGPlayerConfig;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemCommand implements CommandRunner {

    private final List<String> actions = List.of("addlore", "name", "material", "level");
    //temporary until new ITEM API is here

    @Override
    public void runCommand(CommandSender sender, String[] args) {

        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();

        try {
            switch (args[1]) {
                case "addlore" -> {
                    ItemStack newItem = new ItemBuilder(item).addToLore(args[2]).build();
                    p.getInventory().setItemInMainHand(newItem);
                }
                case "name" -> {
                    ItemStack newItem = new ItemBuilder(item).displayname(Chat.translate(args[2])).build();
                    p.getInventory().setItemInMainHand(newItem);
                }

                case "material" -> {
                    p.sendMessage("");
                }

                case "level" -> {
                    RPGPlayerConfig.get().setLevel(p, 23);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            p.sendMessage(Chat.format("pls use command properly idiot :v"));
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 2) {
            return actions.stream().filter(action -> action.startsWith(args[1])).toList();
        }

        if (args.length == 3) {
            switch (args[1]) {
                case "material" -> {
                    return Arrays.stream(Material.values())
                            .map(Enum::name)
                            .filter(name -> name.startsWith(args[2]))
                            .toList();
                }
            }
        }

        return Collections.emptyList();
    }
}
