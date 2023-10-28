package net.haraxx.coresystem.commands.item;

import net.haraxx.coresystem.builder.Chat;
import net.haraxx.coresystem.builder.ItemBuilder;
import net.haraxx.coresystem.plugins.rpg.abilities.Abilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCommand implements ModuleCommandExecutor, TabCompleter {

    static List<String> actions = List.of("item", "test");
    //temporary until new ITEM API is here

    @Override
    public void runCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        ItemStack item = p.getInventory().getItemInMainHand();

        if (args.length <= 4) {
            String action = args[0];
            String param = args[1];
            String param2 = args[2];

            switch (action) {
                case "item" -> {
                    switch (param) {
                        case "addlore" -> {
                            ItemStack newItem = new ItemBuilder(item).addToLore(param2).build();
                            p.getInventory().setItemInMainHand(newItem);
                        }

                        case "name" -> {
                            ItemStack newItem = new ItemBuilder(item).displayname(Chat.translate(param2)).build();
                            p.getInventory().setItemInMainHand(newItem);
                        }

                        case "run" -> {
                            String ability = Abilities.getAbilityByItemStack(item);
                            if (ability != null)
                                Abilities.getAbilitys().get(ability).runAbility(p);
                            else p.sendMessage(Chat.format("this item does not have an ability!"));
                        }

                        case "material" -> {
                            p.sendMessage("");
                        }

                        case "ability" -> {
                        }
                    }
                }

                case "test" -> {
                    p.sendMessage("works");
                }
            }
        } else p.sendMessage("error");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        /*
        String param = strings[1];
        switch (param) {
            case "ability" -> {
                return Abilities.getAbilityNames();
            }

            case "protect" -> {
                return List.of("true", "false");
            }
        }

         */
        return actions;
    }
}
