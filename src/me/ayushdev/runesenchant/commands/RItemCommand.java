package me.ayushdev.runesenchant.commands;

import me.ayushdev.runesenchant.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ritem")) {
            if (!sender.hasPermission("re.admin")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                return true;
            }

            // /ritem <type> <player> <level>

            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /ritem <type> <player> (level)");
                return true;
            }

            if (args[0].equalsIgnoreCase("protection") ||
                    args[0].equalsIgnoreCase("orb")
                    || args[0].equalsIgnoreCase("luck")) {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /ritem " + args[0].toLowerCase() + " <player> <level>");
                    return true;
                }

                Player p = Bukkit.getPlayer(args[1]);
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + "Specified player is not online!");
                    return true;
                }

                int level;
                try {
                    level = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Level must be an integer!");
                    return true;
                }

                ItemStack item;
                Message message;

                if (args[0].equalsIgnoreCase("protection")) {
                    item = new ProtectionCharm(level).createItem();
                    message = Message.PROTECTION_CHARM_RECEIVED;
                } else

                if (args[0].equalsIgnoreCase("orb")) {
                    item = new EnchantmentOrb(level).getItem();
                    message = Message.ENCHANTMENT_ORB_RECEIVED;
                } else {
                    item = new LuckStone(level).getItem();
                    message = Message.LUCK_STONE_RECEIVED;
                }

                p.getInventory().addItem(item);
                MessageManager.getInstance().sendMessage(p, message, new Placeholder("%level%", level),
                        new Placeholder("%slots%", level));

            } else if (args[0].equalsIgnoreCase("resurrection")) {

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /ritem <type> <player>");
                    return true;
                }

                Player p = Bukkit.getPlayer(args[1]);
                if (p == null) {
                    sender.sendMessage(ChatColor.RED + "Specified player is not online!");
                    return true;
                }

                ItemStack item = new ResurrectionStone().getItem();
                p.getInventory().addItem(item);
                MessageManager.getInstance().sendMessage(p, Message.RESURRECTION_STONE_RECEIVED);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();

        if (cmd.getName().equalsIgnoreCase("ritem")) {
            if (args.length == 1) {
                list.add("protection");
                list.add("resurrection");
                list.add("luck");
                list.add("orb");
            } else if (args.length == 2) {
                Bukkit.getOnlinePlayers().forEach(p -> list.add(p.getName()));
            }
        }

        return list;
    }
}
