package me.ayush_03.runesenchant.listeners;

import me.ayush_03.runesenchant.ApplicableItem;
import me.ayush_03.runesenchant.CustomEnchant;
import me.ayush_03.runesenchant.Response;
import me.ayush_03.runesenchant.Rune;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RuneApplyListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (!p.hasPermission("rune.apply")) return;
            if (e.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                if (e.getCursor() == null || e.getCurrentItem() == null) return;

                ItemStack cursor = e.getCursor();
                ItemStack current = e.getCurrentItem();

                if (cursor.getType() == Material.AIR || current.getType() == Material.AIR) return;

                if (Rune.isRune(cursor)) {
                    e.setCancelled(true);
                    Rune rune = new Rune(cursor);
                    CustomEnchant ce = rune.getEnchantment();
                    int level = rune.getLevel();

                    // TODO: ADD Messaging system...

                    if (current.getType().toString().contains("_" + ce.getType().toString())) {
                        ApplicableItem item = new ApplicableItem(current);

                        if (!item.hasEnchantment(ce)) {
                            Response response = item.addEnchantment(ce, level);

                            if (response == Response.SUCCESS) {
                                item.addEnchantment(ce, level);
                                p.setItemOnCursor(new ItemStack(Material.AIR));
                            } else {
                                p.sendMessage(ChatColor.RED + "You do not have any slot left :(");
                                return;
                            }

                        } else {
                            if (item.getLevel(ce) == level) {
                                if (level == ce.getMaxLevel()) {
                                    p.sendMessage(ChatColor.RED + "You are already on a max level!");
                                    return;
                                }

                                item.setLevel(ce, level+1);
                                p.setItemOnCursor(new ItemStack(Material.AIR));
                            } else {
                                p.sendMessage(ChatColor.RED + "You already have an enchantment!");
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean chance(int successRate) {
        int chance = new Random().nextInt(100)+1;
        if (successRate <= chance) return true;
        return false;
    }
}
