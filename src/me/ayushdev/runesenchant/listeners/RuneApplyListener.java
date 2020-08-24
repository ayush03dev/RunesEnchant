package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RuneApplyListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getInventory().getHolder() != null && e.getInventory().getHolder() instanceof GUIHolder) return;

            if (!p.hasPermission("rune.apply")) return;
            if (e.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                if (e.getCursor() == null || e.getCurrentItem() == null) return;

                ItemStack cursor = e.getCursor();
                ItemStack current = e.getCurrentItem();

                if (cursor.getType() == Material.AIR || current.getType() == Material.AIR) return;

                if (current.getAmount() != 1 || cursor.getAmount() != 1) {
                    p.sendMessage(ChatColor.RED + "The amount of Item and Rune cannot be more than 1!");
                    e.setCancelled(true);
                    return;
                }

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

                            if (Settings.getInstance().slotsEnabled()) {
                                if (response == Response.ERROR_NO_SLOT) {
                                    e.setCancelled(true);
                                    p.sendMessage(ChatColor.RED + "You do not have any slot left!");
                                    return;
                                }
                            }

                            if (response == Response.AVAILABLE) {

                                if (chance(rune.getSuccessRate())) {
                                    // Successful
                                    p.setItemOnCursor(new ItemStack(Material.AIR));
                                    if (Settings.getInstance().slotsEnabled()) {
                                        item.setSlots(item.getSlots() - 1);
                                    }
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
                                if (level > item.getLevel(ce)) {
                                    item.setLevel(ce, level);
                                    p.setItemOnCursor(new ItemStack(Material.AIR));

                                }
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

                        if (RunesEnchant.is13()) {
                            PersistentDataContainer pdc = meta.getPersistentDataContainer();
                            pdc.set(new NamespacedKey(RunesEnchant.getInstance(), "re.pc"),
                                    PersistentDataType.STRING, pc.getLevel() + ":" + pc.getLeft()
                            + ":" + lore.size());
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
