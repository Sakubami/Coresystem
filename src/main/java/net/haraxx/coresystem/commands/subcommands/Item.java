package net.haraxx.coresystem.commands.subcommands;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.commands.CommandRunner;
import net.haraxx.coresystem.builder.item.ItemBuilder;
import net.haraxx.coresystem.permissions.Utils;
import net.haraxx.coresystem.plugins.rpg.player.RPGPlayerConfig;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.IntStream;

public class Item implements CommandRunner {

    LuckPerms api = LuckPermsProvider.get();

    private final List<String> actions = List.of("addlore", "editlore","name", "material", "level");

    String args(int value, String[] args) {
        String str = "";
        for (int i = value; i < args.length; i++) {
            str = str +" "+ args[i];
        }
        str = str.trim();
        return str;
    }

    @Override
    public void runCommand(CommandSender sender, String[] args) {

        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();

        if (Utils.getDefaultPerms(p)) {
            try {
                switch (args[1]) {
                    case "addlore" -> {
                        ItemStack newItem = new ItemBuilder(item).addToLore(args(2, args)).build();
                        p.getInventory().setItemInMainHand(newItem);
                    }

                    case "name" -> {
                        ItemStack newItem = new ItemBuilder(item).displayname(Chat.translate(args(2, args))).build();
                        p.getInventory().setItemInMainHand(newItem);
                    }

                    case "editlore" -> {
                        if (item.getItemMeta().getLore() != null) {

                            int index = Integer.parseInt(args[2]);

                            ItemStack newItem = new ItemBuilder(item).editLore(index, Chat.translate(args(3, args))).build();
                            p.getInventory().setItemInMainHand(newItem);
                        }
                    }

                    case "material" -> {
                        ItemStack newItem = new ItemBuilder(Material.getMaterial(args[2].toUpperCase())).build();
                        p.getInventory().setItemInMainHand(newItem);
                    }

                    case "level" -> {
                        RPGPlayerConfig.get().setLevel(p, 23);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                p.sendMessage(Chat.format("pls use command properly idiot :v"));
            }
        } else  p.sendMessage(Chat.format("du hast nicht die nötigen §6Rechte §7um den §6Command §7auszuführen"));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {

        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();

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

                case "editlore" -> {
                    return IntStream.range( 0, item.getItemMeta().getLore().size() ).boxed().map( String::valueOf ).toList();
                }
            }
        }

        if (args.length == 4) {
            if (args[1].equalsIgnoreCase("editlore")) {
                int index = Integer.parseInt(args[2]);
                return List.of(item.getItemMeta().getLore().get(index).replace("§", "&"));
            }
        }

        return Collections.emptyList();
    }
}
