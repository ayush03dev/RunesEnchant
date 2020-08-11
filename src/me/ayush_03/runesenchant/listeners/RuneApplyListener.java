package me.ayush_03.runesenchant.listeners;

import me.ayush_03.runesenchant.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
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

                    if (ce.getType().isApplicableItem(current)) {
                        ApplicableItem item = new ApplicableItem(current);

                        if (!item.hasEnchantment(ce)) {
                            Response response = item.verifyAddEnchantment(ce, level);

                            if (response == Response.ERROR_EXSTS) return;

                            if (response == Response.AVAILABLE) {

                                if (chance(rune.getSuccessRate())) {
                                    // Successful
                                    p.setItemOnCursor(new ItemStack(Material.AIR));
                                    item.addEnchantment(ce, level);
                                    p.sendMessage(ChatColor.GREEN + "SUCCESSFUL! :D");
                                } else {
                                    if (chance(rune.getDestroyRate())) {

                                        if (item.hasProtectionCharm()) {
                                            // Has Protection Charm...
                                            ProtectionCharm pc = new ProtectionCharm(current);
                                            pc.setLeft(pc.getLeft() - 1);
                                            p.setItemOnCursor(new ItemStack(Material.AIR));
                                            p.sendMessage(ChatColor.GRAY + "SAVED YA :)");

                                        } else {
                                            p.sendMessage(ChatColor.RED + "DESTROYED :(");
                                            e.setCurrentItem(new ItemStack(Material.AIR));
                                            p.setItemOnCursor(new ItemStack(Material.AIR));
                                        }

                                    } else {
                                        p.setItemOnCursor(new ItemStack(Material.AIR));
                                        p.sendMessage(ChatColor.RED + "UNSUCCESSFUL MATE :(");
                                    }
                                }

//                                if (item.hasProtectionCharm()) {
//                                    if (Settings.getInstance().slotsEnabled()) {
//                                        item.setSlots(item.getSlots() - 1);
//                                    }
//                                    ProtectionCharm pc = new ProtectionCharm(current);
//                                    pc.setLeft(pc.getLeft() - 1);
//
//                                } else {
//                                    if (!chance(rune.getSuccessRate())) {
//                                        if (chance(rune.getDestroyRate())) {
//                                            // Destroy...
//                                            p.sendMessage(ChatColor.RED + "DESTORYED :(");
//                                            e.setCurrentItem(new ItemStack(Material.AIR));
//                                            p.setItemOnCursor(new ItemStack(Material.AIR));
//                                            current.setType(Material.AIR);
//                                            return;
//                                        }
//                                        p.setItemOnCursor(new ItemStack(Material.AIR));
//                                        p.sendMessage(ChatColor.RED + "UNSUCCESSFUL MATE :(");
//                                        // Remove rune, send message.
//                                        return;
//                                    }
//                                }
//                                p.setItemOnCursor(new ItemStack(Material.AIR));
//                                item.addEnchantment(ce, level);
//                                p.sendMessage(ChatColor.GREEN + "SUCCESSFUL! :D");

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
                    return;
                }

                if (ProtectionCharm.isProtectionCharmItem(cursor)) {
                    String[] data = ProtectionCharm.getProtectionCharmData(cursor);
                    if (data == null) return;
                    int level = Integer.parseInt(data[1]);

                    ApplicableItem item = new ApplicableItem(current);
                    if (!item.hasProtectionCharm()) {
                        ItemMeta meta = current.getItemMeta();
                        if (meta == null) return;
                        e.setCancelled(true);
                        p.setItemOnCursor(new ItemStack(Material.AIR));
                        ProtectionCharm pc = new ProtectionCharm(level);
                        List<String> lore;

                        if (meta.hasLore()) {
                            lore = meta.getLore();
                        } else {
                            lore = new ArrayList<>();
                        }
                        assert lore != null : "Lore is null";
                        pc.addToLore(lore);
                        meta.setLore(lore);
                        current.setItemMeta(meta);
                    }
                }
            }
        }
    }

    private boolean chance(int successRate) {
        int chance = new Random().nextInt(100)+1;
        System.out.println(successRate + " : " + chance);
        if (chance <= successRate) return true;
        return false;
    }
}
