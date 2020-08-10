package me.ayush_03.runesenchant.commands;

import me.ayush_03.runesenchant.CustomEnchant;
import me.ayush_03.runesenchant.Rune;
import me.ayush_03.runesenchant.RunesEnchant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RunesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("runes")) {
            if (args.length == 0) {
                // TODO: Send all available commands...
                return true;
            }

            // TODO: /runes give Ayush_03 <type> <level> (success-rate) (destroy-rate)
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (sender.hasPermission("runes.give")) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target == null) {
                            sender.sendMessage(ChatColor.RED + "Could not find specified player!");
                            return true;
                        }

                        CustomEnchant ce = CustomEnchant.fromString(args[2]);
                        if (ce == null) {
                            sender.sendMessage(ChatColor.RED + "Invalid custom enchantment!");
                            // TODO: Send all available custom enchantments...
                            return true;
                        }

                        int level;

                        try {
                            level = Integer.parseInt(args[3]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Level must be an integer!");
                            return true;
                        }

                        if (level <= 0) {
                            sender.sendMessage(ChatColor.RED + "Level must be a positive integer!");
                            return true;
                        }

                        int maxLevel = ce.getMaxLevel();

                        if (level > maxLevel) {
                            sender.sendMessage(ChatColor.RED + "Max level of the enchantment is " + maxLevel + "!");
                            return true;
                        }

                        RunesEnchant re = RunesEnchant.getInstance();

                        int success = re.getConfig().getInt("success-rate");
                        int destroy = re.getConfig().getInt("destroy-rate");

                        Rune rune = new Rune(ce, level, success, destroy);
                        ItemStack item = rune.getItem();

                        if (target.getInventory().firstEmpty() == -1) {
                            target.getWorld().dropItem(target.getLocation(), item);
                            // TODO: Send message.
                        } else {
                            target.getInventory().addItem(item);
                            // TODO: Send message.
                        }


                    } else {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
