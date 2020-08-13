package me.ayush_03.runesenchant.listeners;

import me.ayush_03.runesenchant.*;
import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchanterGUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            p.sendMessage(e.getAction().toString());
            if (e.getInventory().getHolder() instanceof GUIHolder) {
                GUIHolder holder = new GUIHolder(p);
                if (holder.getPlayer().getUniqueId() == p.getUniqueId()) {
                    Inventory inv = e.getInventory();

                    if (e.getRawSlot() < 45) {
                        // 19, 21, 23, 25 --- 31
                        // I   R    P   L     RESULT
                        if (e.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                            ItemStack cursor = e.getCursor();
                            ItemStack current = e.getCurrentItem();

                            if (cursor == null || cursor.getType() == Material.AIR) return;


                            int slot = e.getRawSlot();
                            int resultSlot = 31; // To be reset later...

                            if (slot == 19) {

                                if (isDemoItem(current)) {
                                    e.setCancelled(true);
                                    e.setCurrentItem(cursor);
                                    p.setItemOnCursor(new ItemStack(Material.AIR));
                                }

                                if (Rune.isRune(inv.getItem(21))) {

                                    // TODO: Manipulate according to luck stone and resurrection stone later..
                                    inv.setItem(resultSlot, new ItemStack(Material.EMERALD_BLOCK));
                                }
                                return;
                            }

                            if (slot == 21) {
                                if (Rune.isRune(cursor)) {
                                    if (isDemoItem(current)) {
                                        e.setCancelled(true);
                                        e.setCurrentItem(cursor);
                                        p.setItemOnCursor(new ItemStack(Material.AIR));
                                    }

                                    if (!isDemoItem(inv.getItem(19))) {
                                        // TODO: Manipulate according to luck stone and resurrection stone later..
                                        inv.setItem(resultSlot, new ItemStack(Material.EMERALD_BLOCK));
                                    }
                                }
                            }
                        } else if (e.getAction() == InventoryAction.PICKUP_ALL) {
                            ItemStack current = e.getCurrentItem();
                            if (current != null && current.getType() != Material.AIR) {
                                // TODO: Change result item...
                                if (!isDemoItem(current)) {

                                }

                            }
                        }


                    } else {
                        ItemStack current = e.getCurrentItem();
                        if (current == null || current.getType() == Material.AIR) return;
                        if (!Rune.isRune(current) && !ResurrectionStone.isRessurectionStone(current)
                                && !LuckStone.isLuckStone(current) && current.getType() != Material.DIAMOND_SWORD) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean isDemoItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return true;
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String display = item.getItemMeta().getDisplayName();
            if (HiddenStringUtils.hasHiddenString(display)) {
                return HiddenStringUtils.extractHiddenString(display).contains("demo");
            }
        }
        return false;
    }

    private EnchanterItemMessage[] getMessages(Inventory inv) {
        // 19, 21, 23, 25 --- 31
        List<EnchanterItemMessage> messageList = new ArrayList<>(4);

        ItemStack item = inv.getItem(19);
        ItemStack rune = inv.getItem(21);
        ItemStack protection = inv.getItem(23);

        if (isDemoItem(item) && isDemoItem(rune)) {
            messageList.add(EnchanterItemMessage.UNCHANGED);;
        } else if (isDemoItem(item)) {
            messageList.add(EnchanterItemMessage.NO_ITEM);
        } else if (isDemoItem(rune)) {
            messageList.add(EnchanterItemMessage.NO_RUNE);

        } else {
            if (isDemoItem(protection)) {
                messageList.add(EnchanterItemMessage.NOT_PROTECTED);
            } else {
                messageList.add(EnchanterItemMessage.PROTECTED);
            }

        }

        return (EnchanterItemMessage[]) messageList.toArray();
    }

    private boolean hasErrors(Inventory inv) {
        for (EnchanterItemMessage msg : getMessages(inv)) {
            if (msg == EnchanterItemMessage.NO_ITEM || msg == EnchanterItemMessage.NO_RUNE
            || msg == EnchanterItemMessage.UNCHANGED) return true;
        }
        return false;
    }

}
