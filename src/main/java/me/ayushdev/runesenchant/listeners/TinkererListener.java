package me.ayushdev.runesenchant.listeners;

import me.ayushdev.runesenchant.*;
import me.ayushdev.runesenchant.inventoryholders.TinkererHolder;
import me.ayushdev.runesenchant.utils.HiddenStringUtils;
import me.ayushdev.runesenchant.utils.RuneUtils;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TinkererListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        if (inv.getHolder() != null && inv.getHolder() instanceof TinkererHolder) {
            TinkererHolder holder = (TinkererHolder) inv.getHolder();
            if (holder.getPlayer().getUniqueId().equals(e.getWhoClicked().getUniqueId())) {
                Player p = holder.getPlayer();
                int raw = e.getRawSlot();

                if (e.getCurrentItem() == null) return;
                ItemStack current = e.getCurrentItem();

                if (raw > 53) {
                    if (ApplicableItem.isSupportedItem(current) || Rune.isRune(current)) {
                        if (inv.firstEmpty() != -1) {
                            int empty = inv.firstEmpty();

                            ItemStack bottle;
                            AcceptButton button = new AcceptButton(inv.getItem(0));
                            int total = button.getTotalXP();
                            if (Rune.isRune(current)) {
                                Rune rune = new Rune(current);
                                bottle = getXPBottle(rune.getEnchantment(), rune.getLevel());
                                total += rune.getEnchantment().getCost(rune.getLevel());

                            } else {
                                ApplicableItem ai = new ApplicableItem(current);
                                if (ai.getAllCustomEnchantments().isEmpty()) {
                                    e.setCancelled(true);
                                    return;
                                }
                                bottle = getXPBottle(ai.getAllCustomEnchantments());
                                total += getTotalXP(ai.getAllCustomEnchantments());
                            }

                            inv.setItem(empty, current);
                            e.setCurrentItem(new ItemStack(Material.AIR));
                            inv.setItem(getCorrespondingSlots().get(empty), bottle);
                            button = new AcceptButton(total);
                            inv.setItem(0, button.getItem());
                            inv.setItem(8, button.getItem());
                        }
                    } else {
                        e.setCancelled(true);
                    }
                } else {

                    if (AcceptButton.isAcceptButton(current)) {
                        e.setCancelled(true);
                        AcceptButton btn = new AcceptButton(current);
                        inv.setItem(0, new ItemStack(Material.AIR)); // Identifier...
                        p.closeInventory();
                        RuneUtils utils = RuneUtils.getInstance();
                        utils.setTotalExperience(btn.getTotalXP() + utils.getTotalExperience(p), p);
                        MessageManager.getInstance().sendMessage(p, Message.TINKERER_SUCCESS,
                                new Placeholder("%xp%", btn.getTotalXP() + ""));
                        return;
                    }

                    if (getCorrespondingSlots().containsKey(raw)) {
                        if (p.getInventory().firstEmpty() != -1) {

                            AcceptButton button = new AcceptButton(inv.getItem(0));
                            int total = button.getTotalXP();

                            if (Rune.isRune(current)) {
                                Rune rune = new Rune(current);
                                total -= rune.getEnchantment().getCost(rune.getLevel());
                            } else {
                                total -= getTotalXP(new ApplicableItem(current).getAllCustomEnchantments());
                            }

                            button = new AcceptButton(total);
                            inv.setItem(0, button.getItem());
                            inv.setItem(8, button.getItem());

                            p.getInventory().addItem(current);
                            p.updateInventory();
                            inv.setItem(getCorrespondingSlots().get(raw), new ItemStack(Material.AIR));
                            e.setCurrentItem(new ItemStack(Material.AIR));
                        } else {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof  Player) {
            Player p = (Player) e.getPlayer();
            Inventory inv = e.getInventory();

            if (inv.getHolder() != null && inv.getHolder() instanceof TinkererHolder) {
                TinkererHolder holder = (TinkererHolder) inv.getHolder();
                if (holder.getPlayer().getUniqueId().equals(p.getUniqueId())) {
                    if (inv.getItem(0) == null || inv.getItem(0).getType() == Material.AIR) return;

                    boolean full = false;

                    for (int slots : getCorrespondingSlots().keySet()) {
                        ItemStack item = inv.getItem(slots);
                        if (item != null && item.getType() != Material.AIR) {
                            if (p.getInventory().firstEmpty() != -1) {
                                p.getInventory().addItem(item);
                            } else {
                                if (!full) {
                                    full = true;
                                }
                                p.getWorld().dropItem(p.getLocation(), item);
                            }
                        }
                    }

                    if (full) {
                        MessageManager.getInstance().sendMessage(p, Message.ITEM_DROPPED);

                    }
                    p.updateInventory();
                }
            }
        }
    }

    private Map<Integer, Integer> getCorrespondingSlots() {
        Map<Integer, Integer> slots = new HashMap<>();
        slots.put(1, 5);
        slots.put(2, 6);
        slots.put(3, 7);
        slots.put(9, 14);
        slots.put(10, 15);
        slots.put(11, 16);
        slots.put(12, 17);
        slots.put(18, 23);
        slots.put(19, 24);
        slots.put(20, 25);
        slots.put(21, 26);
        slots.put(27, 32);
        slots.put(28, 33);
        slots.put(29, 34);
        slots.put(30, 35);
        slots.put(36, 41);
        slots.put(37, 42);
        slots.put(38, 43);
        slots.put(39, 44);
        slots.put(45, 50);
        slots.put(46, 51);
        slots.put(47, 52);
        slots.put(48, 53);
        return slots;
    }

    private ItemStack getXPBottle(CustomEnchant ce, int level) {
        ItemStack item;
        if (RunesEnchant.is13()) {
             item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        } else {
            item = new ItemStack(Material.matchMaterial("EXP_BOTTLE"));
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "" + ce.getCost(level) + " XP");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack getXPBottle(Map<CustomEnchant, Integer> enchants) {
        ItemStack item;
        if (RunesEnchant.is13()) {
            item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        } else {
            item = new ItemStack(Material.matchMaterial("EXP_BOTTLE"));
        }
        ItemMeta meta = item.getItemMeta();
        int cost = 0;
        for (Map.Entry<CustomEnchant, Integer> entry : enchants.entrySet()) {
            cost += entry.getKey().getCost(entry.getValue());
        }

        meta.setDisplayName(ChatColor.GREEN + "" + cost + " XP");
        item.setItemMeta(meta);
        return item;
    }

    private int getTotalXP(Map<CustomEnchant, Integer> enchants) {
        int cost = 0;
        for (Map.Entry<CustomEnchant, Integer> entry : enchants.entrySet()) {
            cost += entry.getKey().getCost(entry.getValue());
        }
        return cost;
    }

    private void update(Inventory inv, int xp) {
        ItemStack btn = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = btn.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "Accept Trade");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Total XP: " + ChatColor.GREEN + "" + xp);
        meta.setLore(lore);
        btn.setItemMeta(meta);
        inv.setItem(0, btn);
        inv.setItem(8, btn);
    }
}

class AcceptButton {

    int total;
    ItemStack item;

    public AcceptButton(int total) {
        this.total = total;

//        ItemStack btn = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemStack btn;
        if (RunesEnchant.is13()) {
            btn = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        } else {
            btn = new ItemStack(Material.matchMaterial("STAINED_GLASS_PANE"), 1, (byte) 13);
        }
        ItemMeta meta = btn.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.YELLOW + "Total XP: " + ChatColor.GREEN + "" + total);
        meta.setLore(lore);

        if (RunesEnchant.is13()) {
            meta.setDisplayName(ChatColor.YELLOW + "Accept Trade");
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(RunesEnchant.getInstance(), "re.tinkerer"), PersistentDataType.INTEGER, total);
        } else {
            meta.setDisplayName(ChatColor.YELLOW + "Accept Trade" + HiddenStringUtils.encodeString("re.tinkerer:" + total));
        }

        btn.setItemMeta(meta);
        this.item = btn;
    }

    public AcceptButton(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (RunesEnchant.is13()) {
                PersistentDataContainer data = meta.getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(RunesEnchant.getInstance(), "re.tinkerer");
                if (data.has(key, PersistentDataType.INTEGER)) {
                    this.total = data.get(key, PersistentDataType.INTEGER);
                }
            } else {
                if (meta.hasDisplayName() && HiddenStringUtils.hasHiddenString(meta.getDisplayName())) {
                    String data = HiddenStringUtils.extractHiddenString(meta.getDisplayName());
                    if (data.contains("re.tinkerer:")) {
                        data = data.replace("re.tinkerer:", "");
                        this.total = Integer.parseInt(data);
                    }
                }
            }
            this.item = item;
        }
    }

    public int getTotalXP() {
        return total;
    }

    public ItemStack getItem() {
        return item;
    }

    public static boolean isAcceptButton(ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (RunesEnchant.is13()) {
                return meta.getPersistentDataContainer().has(new NamespacedKey(RunesEnchant.getInstance(),
                        "re.tinkerer"), PersistentDataType.INTEGER);
            } else {
                return meta.hasDisplayName() &&
                        HiddenStringUtils.hasHiddenString(meta.getDisplayName()) &&
                        HiddenStringUtils.extractHiddenString(meta.getDisplayName()).contains("re.tinkerer:");
            }
        }

        return false;
    }
}
