package me.ayushdev.runesenchant.commands;

import me.ayushdev.runesenchant.CustomEnchant;
import me.ayushdev.runesenchant.Rune;
import me.ayushdev.runesenchant.RuneConfig;
import me.ayushdev.runesenchant.gui.EnchanterGUI;
import me.ayushdev.runesenchant.utils.RuneUtils;
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
import java.util.Random;

public class RunesCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("runes")) {
            if (args.length == 0) {
                // TODO: Send all available commands...
                return true;
            }

            if (args.length >= 3) {
                if (args[0].equalsIgnoreCase("give")) {

                    if (!sender.hasPermission("runes.give")) {
                        sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Could not find player " +
                                "the specified player.");
                        return true;
                    }

                    CustomEnchant ce = CustomEnchant.fromString(args[2]);
                    if (ce == null) {
                        sender.sendMessage(ChatColor.RED + "Invalid custom enchantment id!");
                        sender.sendMessage("§eUse §b/runes enchants §ecommand to get the list of all enchants.");
                        return true;
                    }

                    if (args.length == 3) {
                        int level = new Random().nextInt(ce.getMaxLevel()) + 1;
                        Rune rune = new Rune(ce, level);
                        ItemStack item = rune.getItem();

                        if (target.getInventory().firstEmpty() == -1) {
                            target.getWorld().dropItem(target.getLocation(), item);

                            // TODO: Send message

                        } else {
                            target.getInventory().addItem(rune.getItem());
                        }

                        // TODO: Send message...
                    }

                    // TODO: /runes give Ayush_03 <type> <level> (success-rate) (destroy-rate)
                    if (args.length >= 4) {
                        int level;

                        if (args[3].equalsIgnoreCase("random")) {
                            level = new Random().nextInt(ce.getMaxLevel()) + 1;

                        } else {
                            try {
                                level = Integer.parseInt(args[3]);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.RED + "Level must be an integer!");
                                return true;
                            }
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

                        Rune rune;
                        RuneUtils utils = RuneUtils.getInstance();

                        if (args.length == 4) {
                            rune = new Rune(ce, level);
                        } else if (args.length == 5) {
                            int successRate;
                            if (args[4].equalsIgnoreCase("random")) {
                                 successRate = utils.getRandomRate();
                            } else {
                                try {
                                     successRate = Integer.parseInt(args[4]);
                                } catch (NumberFormatException ex) {
                                    sender.sendMessage(ChatColor.RED + "Success rate must be an integer!");
                                    return true;
                                }
                            }

                            int destroyRate = new RuneConfig(ce).getDestroyRate();
                            rune = new Rune(ce, level, successRate, destroyRate);
                        } else {
                            int successRate, destroyRate;

                            if (args[4].equalsIgnoreCase("random")) {
                                successRate = utils.getRandomRate();
                            } else {
                                try {
                                    successRate = Integer.parseInt(args[4]);
                                } catch (NumberFormatException ex) {
                                    sender.sendMessage(ChatColor.RED + "Rates must be integers!");
                                    return true;
                                }
                            }

                            if (args[5].equalsIgnoreCase("random")) {
                                destroyRate = utils.getRandomRate();
                            } else {
                                try {
                                    destroyRate = Integer.parseInt(args[5]);
                                } catch (NumberFormatException ex) {
                                    sender.sendMessage(ChatColor.RED + "Rates must be integers!");
                                    return true;
                                }
                            }

                            rune = new Rune(ce, level, successRate, destroyRate);
                        }

                        ItemStack item = rune.getItem();

                        if (target.getInventory().firstEmpty() == -1) {
                            target.getWorld().dropItem(target.getLocation(), item);
                            // TODO: Send message.
                        } else {
                            target.getInventory().addItem(item);
                            // TODO: Send message.
                        }
                    }
                }
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("enchanter")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (!p.hasPermission("runes.enchanter")) {
                            p.sendMessage(ChatColor.RED + "You do not have permission" +
                                    "to execute this command!");
                            return true;
                        }
                        p.openInventory(new EnchanterGUI().createEnchanterGUI(p));
                    } else {
                        sender.sendMessage(ChatColor.RED + "This command can only" +
                                "be executed by players!");
                    }
                } else {
                    // TODO Send all available commands...
                    return true;
                }
            }

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (cmd.getName().equalsIgnoreCase("runes")) {

            if (args.length == 1) {
                list.add("give");
                list.add("enchanter");
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    Bukkit.getServer().getOnlinePlayers().forEach(p -> {
                        list.add(p.getName());
                    });
                }
            } else if (args.length == 3) {
                // TODO: Get all the enabled enchantments...
                for (CustomEnchant ce : CustomEnchant.values()) {
                    list.add(ce.toString());
                }
            } else if (args.length == 4) {
                CustomEnchant ce = CustomEnchant.fromString(args[2]);
                if (ce != null) {
                    for (int i = 0; i < ce.getMaxLevel(); i++) {
                        list.add((i + 1) + "");
                    }
                    list.add("random");
                }
            } else if (args.length == 5 || args.length == 6) {
                list.add("random");
            }
        }
        return list;
    }
}
