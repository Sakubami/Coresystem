package net.haraxx.coresystem.commands.item;

import net.haraxx.coresystem.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemCommand implements CommandExecutor{

    //temporary until new ITEM API is here

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        Player p = (Player) sender;
        ItemStack item;

        if (cmd.getName().equalsIgnoreCase("core")) {
            if (args.length == 2)
                item = new ItemBuilder(Material.getMaterial(args[1].toUpperCase())).build();
            if (args.length == 3) {
                ArrayList<String> newLore = new ArrayList<>();
                newLore.add(args[2]);
                item = new ItemBuilder(Material.getMaterial(args[1].toUpperCase())).lore(newLore).ability("§0BSK_tomahawk").build();
            }
            else {
                p.sendMessage("something");
            }
        }
        return false;
    }
}
