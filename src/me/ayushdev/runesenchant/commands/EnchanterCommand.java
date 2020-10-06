package me.ayushdev.runesenchant.commands;

import me.ayushdev.runesenchant.gui.EnchanterGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnchanterCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("enchanter")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("runes.enchanter")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                    return true;
                }

                p.openInventory(new EnchanterGUI().createEnchanterGUI(p));
            }
        }

        return true;
    }
}
