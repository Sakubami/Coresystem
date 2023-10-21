package net.haraxx.coresystem.commands.item;

import net.haraxx.coresystem.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemCommand implements CommandExecutor {

    //temporary until new ITEM API is here

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        ItemStack item;

        if (args.length == 1)
            item = new ItemBuilder(Material.getMaterial(args[0].toUpperCase())).build();
        if (args.length == 2) {
            ArrayList<String> newLore = new ArrayList<>();
            newLore.add(args[1]);
            item = new ItemBuilder(Material.getMaterial(args[0].toUpperCase())).lore(newLore).ability("§0BSK_tomahawk").build();
        }
        return false;
    }
}
