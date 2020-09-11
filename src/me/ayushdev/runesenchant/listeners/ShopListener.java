package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.EnchantmentGroup;
import me.ayushdev.runesenchant.Rune;
import me.ayushdev.runesenchant.RunesEnchant;
import me.ayushdev.runesenchant.inventoryholders.ShopGUIHolder;
import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import me.ayushdev.runesenchant.utils.RuneUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


public class ShopListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() != null) {
            if (e.getInventory().getHolder() instanceof ShopGUIHolder) {
                ShopGUIHolder holder = (ShopGUIHolder) e.getInventory().getHolder();
                if (e.getWhoClicked() instanceof Player) {
                    Player p = (Player) e.getWhoClicked();
                    if (p.getUniqueId().toString().equals(holder.getPlayer().getUniqueId().toString())) {
                        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                            ItemStack current = e.getCurrentItem();
                            if (!current.hasItemMeta()) return;
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

                            if (group == null) return;
                            e.setCancelled(true);

                            int price = group.getPrice();
                            RuneUtils utils = RuneUtils.getInstance();

                            if (utils.getTotalExperience(p) < price) {
                                p.sendMessage(ChatColor.RED + "You do not have enough xp!");
                                return;
                            }

                            utils.setTotalExperience(utils.getTotalExperience(p) - price, p);
                            Rune rune = group.getRandomRune();
                            p.closeInventory();;
                            p.getInventory().addItem(rune.getItem());
                        }
                    }
                }
            }
        }
    }

}
