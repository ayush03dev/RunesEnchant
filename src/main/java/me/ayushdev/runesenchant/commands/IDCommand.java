package me.ayushdev.runesenchant.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IDCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("id")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("runes.admin")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                    return true;
                }

                ItemStack hand = p.getItemInHand();
                if (hand == null || hand.getType() == Material.AIR) {
                    p.sendMessage(ChatColor.RED + "Please hold something in your hand!");
                    return true;
                }

                p.sendMessage("§aPlease use the following item id: §e" + hand.getType().toString() + ":" + hand.getDurability());
            }
        }
        return true;
    }
}
