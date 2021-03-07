package me.ayushdev.runesenchant.commands;

import me.ayushdev.runesenchant.gui.Tinkerer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TinkererCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tinkerer")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("runes.tinkerer")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                    return true;
                }

                new Tinkerer().openInventory(p);
            }
        }
        return true;
    }

}
