package me.ayushdev.runesenchant.commands;

import me.ayushdev.runesenchant.ApplicableItem;
import me.ayushdev.runesenchant.Settings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSlotsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setslots")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (!p.hasPermission("runes.admin")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                    return true;
                }

                if (p.getItemInHand() == null || !ApplicableItem.isSupportedItem(p.getItemInHand())) {
                    p.sendMessage(ChatColor.RED + "You do not have a valid item in your hand!");
                    return true;
                }

                if (args.length != 1) {
                    p.sendMessage(ChatColor.RED + "Usage: /setslots <slots>");
                    return true;
                }

                int slots;
                try {
                    slots = Integer.parseInt(args[0]);
                } catch (NumberFormatException ex) {
                    p.sendMessage(ChatColor.RED + "The slots argument must be an integer!");
                    return true;
                }

                if (!Settings.getInstance().slotsEnabled()) {
                    p.sendMessage(ChatColor.RED + "Slots are disabled on this server!");
                    return true;
                }

                new ApplicableItem(p.getItemInHand()).setSlots(slots);
                p.sendMessage(ChatColor.GREEN + "Number of slots on the item in hand has been set to " + slots + '!');

            }
        }
        return true;
    }

}
