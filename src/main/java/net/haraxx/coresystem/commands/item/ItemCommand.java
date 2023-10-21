package net.haraxx.coresystem.commands.item;

import net.haraxx.coresystem.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class ItemCommand implements CommandExecutor{

    //temporary until new ITEM API is here

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        ItemStack item;

        p.sendMessage(cmd+"");
        if (args.length == 2) {
            p.sendMessage(args.length+"");
            item = new ItemBuilder(Material.getMaterial(args[1].toUpperCase())).build();
            p.getInventory().addItem(item);
        }
        if (args.length == 3) {
            ArrayList<String> newLore = new ArrayList<>();
            newLore.add(args[2]);
            item = new ItemBuilder(Material.getMaterial(args[1].toUpperCase())).lore(newLore).ability("§0BSK_tomahawk").build();
            p.getInventory().addItem(item);
        }
        else {
            p.sendMessage("something");
        }
        return false;
    }
}
