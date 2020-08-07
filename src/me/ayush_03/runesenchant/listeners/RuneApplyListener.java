package me.ayush_03.runesenchant.listeners;

import me.ayush_03.runesenchant.ApplicableItem;
import me.ayush_03.runesenchant.CustomEnchant;
import me.ayush_03.runesenchant.Rune;
import me.ayush_03.runesenchant.utils.HiddenStringUtils;
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

                    if (current.getType().toString().contains(ce.getType().toString())) {
                        ApplicableItem item = new ApplicableItem(current);

                        if (!item.hasEnchantment(ce)) {
                            boolean success = item.addEnchantment(ce, level);

                            if (success) {
                                item.addEnchantment(ce, level);
                                p.setItemOnCursor(new ItemStack(Material.AIR));
                            }
                        } else {

                        }
                    }

                    Random rand = new Random();


                    p.sendMessage(HiddenStringUtils.extractHiddenString(cursor.getItemMeta().getDisplayName()));

                    if ((rand.nextInt(100) + 1) < rune.getSuccessRate()) {
                        // Successful

                    } else {
                        // Unsuccessful
                        if ((rand.nextInt(100) + 1) < rune.getDestroyRate()) {
                            // TODO: Check if item has a protection..
                        }

                    }
                }
            }
        }
    }
}
