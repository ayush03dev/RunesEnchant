package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.EnchantmentGroup;
import me.ayushdev.runesenchant.RunesEnchant;
import me.ayushdev.runesenchant.gui.EnchantmentsGUI;
import me.ayushdev.runesenchant.inventoryholders.EnchantmentsGUIHolder;
import me.ayushdev.runesenchant.inventoryholders.EnchantmentsMenuHolder;
import me.ayushdev.runesenchant.inventoryholders.EnchantmentsSearchGUIHolder;
import me.ayushdev.runesenchant.inventoryholders.GUIHolder;
import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class EnchantmentsGUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getInventory().getHolder() instanceof EnchantmentsGUIHolder) {
                e.setCancelled(true);
                EnchantmentsGUIHolder holder = (EnchantmentsGUIHolder) e.getInventory().getHolder();
                Inventory inv = e.getInventory();

                if (e.getCurrentItem() == null) return;
                ItemStack current = e.getCurrentItem();
                int page = holder.getCurrentPage();
                EnchantmentGroup group = holder.getGroup();

                if (current.hasItemMeta() && current.getItemMeta().hasDisplayName()) {
                    ItemMeta meta = current.getItemMeta();
                    if (current.getType() == Material.ARROW) {
                        if (meta.getDisplayName().equalsIgnoreCase("Next Page")) {
                            EnchantmentsGUI gui = new EnchantmentsGUI(group);
                            if (gui.pageExists(page + 1)) {
                                gui.openGroupGUI(p, group, page + 1);
                            }
                        } else if (meta.getDisplayName().equalsIgnoreCase("Previous Page")) {
                            EnchantmentsGUI gui = new EnchantmentsGUI(group);
                            if (gui.pageExists(page-1)) {
                                gui.openGroupGUI(p, group, page-1);
                            }
                        }
                    } else if (current.getType() == Material.PAPER) {
                         if (meta.getDisplayName().equalsIgnoreCase("Search")) {
                            p.closeInventory();
                            RunesEnchant.search.put(p, group);
                            p.sendMessage(ChatColor.YELLOW + "Enter a string to search for enchantments:");
                        }
                    }
                }
            } else if (e.getInventory().getHolder() instanceof EnchantmentsSearchGUIHolder) {
                e.setCancelled(true);
                EnchantmentsSearchGUIHolder holder = (EnchantmentsSearchGUIHolder) e.getInventory().getHolder();
                Inventory inv = e.getInventory();

                if (e.getCurrentItem() == null) return;
                ItemStack current = e.getCurrentItem();
                int page = holder.getCurrentPage();

                if (current.hasItemMeta() && current.getItemMeta().hasDisplayName()) {
                    ItemMeta meta = current.getItemMeta();
                    if (current.getType() == Material.ARROW) {
                        if (meta.getDisplayName().equalsIgnoreCase("Next Page")) {
                            EnchantmentsGUI gui = new EnchantmentsGUI();
                            if (gui.pageExists(page + 1)) {
                                gui.openGroupGUI(p, holder.getList(), page + 1);
                            }
                        } else if (meta.getDisplayName().equalsIgnoreCase("Previous Page")) {
                            EnchantmentsGUI gui = new EnchantmentsGUI();
                            if (gui.pageExists(page-1)) {
                                gui.openGroupGUI(p, holder.getList(), page-1);
                            }
                        }
                    }
                }
            } else if (e.getInventory().getHolder() instanceof EnchantmentsMenuHolder) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null) {
                    ItemStack current = e.getCurrentItem();
                    if (current.hasItemMeta()) {
                        ItemMeta meta = current.getItemMeta();
                        EnchantmentGroup group = null;

                        if (RunesEnchant.is13()) {
                            NamespacedKey key = new NamespacedKey(RunesEnchant.getInstance(),
                                    "re.group");
                            if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                                String data = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
                                group = new EnchantmentGroup(data);
                            }
                        } else {
                            if (HiddenStringUtils.hasHiddenString(meta.getDisplayName())) {
                                String data = HiddenStringUtils.extractHiddenString(meta.getDisplayName()).replace(
                                        "re.group.", "");
                                group = new EnchantmentGroup(data);
                            }
                        }

                        if (group != null) {
                            p.openInventory(new EnchantmentsGUI(group).openGroupGUI(p, group, 1));
                        }
                    }
                }
            }
        }
    }
}
