package me.ayush_03.runesenchant.listeners;

import me.ayush_03.runesenchant.*;
import me.ayush_03.runesenchant.gui.EnchanterGUI;
import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnchanterListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (e.getInventory().getHolder() instanceof GUIHolder) {
                GUIHolder holder = new GUIHolder(p);
                if (holder.getPlayer().getUniqueId() == p.getUniqueId()) {
                    Inventory inv = e.getInventory();
                    int resultSlot = 40; // To be reset later...
                    int slot = e.getRawSlot();

                    if (slot == resultSlot) {
                        ItemStack btn = e.getCurrentItem();
                        if (btn.getType() == Material.REDSTONE_BLOCK || btn.getType() == Material.BARRIER) {
                            e.setCancelled(true);
                            return;
                        }

                        if (btn.getType() == Material.EMERALD_BLOCK) {

                            e.setCancelled(true);

                            ItemStack rStone = inv.getItem(23);
                            ItemStack lStone = inv.getItem(25);
                            ResurrectionStone rs = null;
                            LuckStone ls = null;

                            if (!isDemoItem(rStone)) {
                                rs = new ResurrectionStone();
                            }

                            if (!isDemoItem(lStone)) {
                                ls = new LuckStone(lStone);
                            }

                            Rune rune = new Rune(inv.getItem(21));
                            ApplicableItem item = new ApplicableItem(inv.getItem(19));

                            int netSuccess = rune.getSuccessRate();
                            if (ls != null) {
                                netSuccess += +ls.getIncrease();
                            }
                            int destroy = rune.getDestroyRate();
                            CustomEnchant ce = rune.getEnchantment();

                            int level = item.getLevel(ce);

                            if (chance(netSuccess)) {

                                if (level == 0) {
                                    item.addEnchantment(ce, level + 1);
                                } else {
                                    int setLevel;
                                    if (rune.getLevel() == level) {
                                        setLevel = level + 1;
                                    } else {
                                        setLevel = rune.getLevel();
                                    }
                                    item.setLevel(ce, setLevel);
                                }

                                for (int i = 0; i < 54; i++) {
                                    inv.setItem(i, getGlassPane(GlassColor.GREEN));
                                }

                                inv.setItem(resultSlot, item.getItemStack());

                            } else {
                                for (int i = 0; i < 54; i++) {
                                    inv.setItem(i, getGlassPane(GlassColor.RED));
                                }
                                if (chance(destroy)) {

                                    if (rs != null) {
                                        inv.setItem(resultSlot, item.getItemStack());
                                        return;
                                    }

                                    // TODO: Check if protection charm is enabled or not..

                                    if (item.hasProtectionCharm()) {
                                        ItemStack itemStack = item.getItemStack();
                                        ProtectionCharm pc = new ProtectionCharm(itemStack);
                                        pc.setLeft(pc.getLeft() - 1);
                                        inv.setItem(resultSlot, itemStack);
                                        return;
                                    }

                                    inv.setItem(resultSlot, new ItemStack(Material.BARRIER));
                                } else {
                                    inv.setItem(resultSlot, item.getItemStack());
                                }
                            }
                        }
                        return;
                    }

                    if (e.getRawSlot() < 45) {
                        if (e.getAction() != InventoryAction.SWAP_WITH_CURSOR &&
                                e.getAction() != InventoryAction.PICKUP_ALL &&
                                e.getAction() != InventoryAction.PLACE_ALL) {
                            e.setCancelled(true);
                            return;
                        }

                        if (isGlass(e.getCurrentItem())) {
                            e.setCancelled(true);
                            return;
                        }
                        // 19, 21, 23, 25 --- 48
                        // I   R    P   L     RESULT
                        if (e.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                            ItemStack cursor = e.getCursor();
                            ItemStack current = e.getCurrentItem();

                            if (cursor == null || cursor.getType() == Material.AIR) return;

                            if (slot == 19) {
                                if (cursor.getType() != Material.DIAMOND_SWORD) {
                                    e.setCancelled(true);
                                    return;
                                }
                            }

                            if (slot == 21 && !Rune.isRune(cursor)) {
                                e.setCancelled(true);
                                return;
                            }
                            if (slot == 23 && !ResurrectionStone.isRessurectionStone(cursor)) {
                                e.setCancelled(true);
                                return;
                            }
                            if (slot == 25 && !LuckStone.isLuckStone(cursor)) {
                                e.setCancelled(true);
                                return;
                            }

                            if (isDemoItem(current)) {
                                e.setCancelled(true);
                                e.setCurrentItem(cursor);
                                p.setItemOnCursor(new ItemStack(Material.AIR));
                            }

                            // TODO: TO fix this, we need to add delay.
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    update(inv, resultSlot);
                                }
                            }.runTaskLater(RunesEnchant.getInstance(), 5);

//                            new BukkitRunnable() {
//
//                                @Override
//                                public void run() {
//
//                                    if (hasErrors(inv)) {
//                                        inv.setItem(resultSlot, new ItemStack(Material.REDSTONE_BLOCK));
//                                        new EnchanterResultant(inv.getItem(resultSlot)).setMessages(getMessages(inv));
//                                        return;
//                                    }
//
//                                    inv.setItem(resultSlot, new ItemStack(Material.EMERALD_BLOCK));
//                                    Rune rune = new Rune(inv.getItem(21));
//                                    ApplicableItem item = new ApplicableItem(inv.getItem(19));
//                                    int netLevel= rune.getLevel() + item.getLevel(rune.getEnchantment());
//                                    LuckStone ls = null;
//
//                                    if (LuckStone.isLuckStone(inv.getItem(25))) {
//                                        ls = new LuckStone(inv.getItem(25));
//                                    }
//
////                                    new EnchanterResultant(inv.getItem(resultSlot)).setMessages(getMessages(inv));
//                                    new EnchanterResultant(inv.getItem(resultSlot)).setMessages(rune, netLevel, ls, getMessages(inv));
//
//                                }
//                             }.runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("RunesEnchant"), 5);


                        } else if (e.getAction() == InventoryAction.PICKUP_ALL) {
                            ItemStack current = e.getCurrentItem();
                            if (isDemoItem(current)) {
                                e.setCancelled(true);
                                return;
                            }

                            ItemStack demo;
                            EnchanterGUI gui = new EnchanterGUI();

                            if (slot == 19) {
                                demo = gui.getDemoItem();
                            } else if (slot == 21) {
                                demo = gui.getDemoRune();
                            } else if (slot == 23) {
                                demo = gui.getDemoResurrectionStone();
                            } else if (slot == 25) {
                                demo = gui.getDemoLuckStone();
                            } else {
                                demo = null;
                            }

                            new BukkitRunnable() {

                                @Override
                                public void run() {

                                    update(inv, resultSlot);
                                    e.setCurrentItem(demo);
                                }
                            }.runTaskLater(RunesEnchant.getInstance(), 5);


//                            new BukkitRunnable() {
//
//                                @Override
//                                public void run() {
//                                    e.setCurrentItem(demo);
//                                    if (hasErrors(inv)) {
//                                                inv.setItem(resultSlot, new ItemStack(Material.REDSTONE_BLOCK));
//                                                new EnchanterResultant(inv.getItem(resultSlot)).setMessages(getMessages(inv));
//                                                return;
//                                            }
//
//                                    inv.setItem(resultSlot, new ItemStack(Material.EMERALD_BLOCK));
//                                    ApplicableItem item = new ApplicableItem(inv.getItem(19));
//                                    Rune rune = new Rune(inv.getItem(21));
//                                    int netLevel = rune.getLevel() + item.getLevel(rune.getEnchantment());
//
//                                    LuckStone ls = new LuckStone(inv.getItem(25));
////                                    new EnchanterResultant(inv.getItem(resultSlot)).setMessages(getMessages(inv));
//                                    new EnchanterResultant(inv.getItem(resultSlot)).setMessages(rune, netLevel, ls, getMessages(inv));
//
//                                }
//                            }.runTaskLaterAsynchronously(Bukkit.getPluginManager().getPlugin("RunesEnchant"), 5);

                        }

                    } else {
                        ItemStack current = e.getCurrentItem();
                        if (current == null || current.getType() == Material.AIR) return;
                        if (!Rune.isRune(current) && !ResurrectionStone.isRessurectionStone(current)
                                && !LuckStone.isLuckStone(current) && current.getType() != Material.DIAMOND_SWORD) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player p = (Player) e.getPlayer();
            Inventory inv = e.getInventory();
            if (inv.getHolder() != null && inv.getHolder() instanceof GUIHolder) {
                GUIHolder holder = new GUIHolder(p);

                if (holder.getPlayer().getUniqueId().equals(p.getUniqueId())) {
                    for (ItemStack item : inv.getContents()) {
                        if (item != null && item.getType() != Material.AIR) {
                            if (!isGlass(item) && !isDemoItem(item)
                                    && item.getType() != Material.EMERALD_BLOCK &&
                                    item.getType() != Material.REDSTONE_BLOCK &&
                                    item.getType() != Material.BARRIER) {
                                p.getWorld().dropItem(p.getLocation(), item);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isDemoItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return true;
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {

            if (RunesEnchant.is13()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                return data.has(new NamespacedKey(RunesEnchant.getInstance(), "re.demo"), PersistentDataType.STRING);
            } else {
                String display = item.getItemMeta().getDisplayName();
                if (HiddenStringUtils.hasHiddenString(display)) {
                    return HiddenStringUtils.extractHiddenString(display).contains("demo");
                }
            }
        }
        return false;
    }

    private boolean isGlass(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return true;
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (RunesEnchant.is13()) {
                PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();
                return data.has(new NamespacedKey(RunesEnchant.getInstance(), "re.glass"), PersistentDataType.STRING);
            } else {
                String display = item.getItemMeta().getDisplayName();
                if (HiddenStringUtils.hasHiddenString(display)) {
                    return HiddenStringUtils.extractHiddenString(display).contains("glass");
                }
            }
        }
        return false;
    }

    private ItemStack getGlassPane(GlassColor color) {
        ItemStack item;
        Material mat;

        if (color == GlassColor.GREEN) {
            try {
                mat = Material.GREEN_STAINED_GLASS_PANE;
                item = new ItemStack(mat);
            } catch (NoSuchFieldError e) {
                mat = Material.matchMaterial("STAINED_GLASS_PANE");
                item = new ItemStack(mat, 1, (byte) 13);
            }
        } else {
            try {
                mat = Material.RED_STAINED_GLASS_PANE;
                item = new ItemStack(mat);
            } catch (NoSuchFieldError e) {
                mat = Material.matchMaterial("STAINED_GLASS_PANE");
                item = new ItemStack(mat, 1, (byte) 14);
            }
        }

        ItemMeta meta = item.getItemMeta();

        if (RunesEnchant.is13()) {
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(new NamespacedKey(RunesEnchant.getInstance(), "re.glass"), PersistentDataType.STRING, "true");
            meta.setDisplayName(ChatColor.BLACK + "");
        } else {
            meta.setDisplayName(HiddenStringUtils.encodeString("glass"));
        }
        item.setItemMeta(meta);
        return item;
    }

    private EnchanterItemMessage[] getMessages(Inventory inv) {
        // 19, 21, 23, 25 --- 31
        List<EnchanterItemMessage> messageList = new ArrayList<>(4);

        ItemStack item = inv.getItem(19);
        ItemStack rune = inv.getItem(21);
        ItemStack protection = inv.getItem(23);
        ItemStack luck = inv.getItem(25);

        if (isDemoItem(item) && isDemoItem(rune)) {
            messageList.add(EnchanterItemMessage.UNCHANGED);
            ;
        } else if (isDemoItem(item)) {
            messageList.add(EnchanterItemMessage.NO_ITEM);
        } else if (isDemoItem(rune)) {
            messageList.add(EnchanterItemMessage.NO_RUNE);

        } else {

            if (!new Rune(rune).getEnchantment().getType().isApplicableItem(item)) {
                messageList.add(EnchanterItemMessage.NOT_APPLICABLE);
            } else {

                Rune r = new Rune(rune);
                ApplicableItem ai = new ApplicableItem(item);

                int level = ai.getLevel(r.getEnchantment());
                if (level != 0) {
                    if (level > r.getLevel()) {
                        messageList.add(EnchanterItemMessage.LOW_LEVEL);
                        return toArray(messageList);
                    } else if (level == r.getEnchantment().getMaxLevel()) {
                        messageList.add(EnchanterItemMessage.MAX_LEVEL);
                        return toArray(messageList);
                    }
                }

                messageList.add(EnchanterItemMessage.RESULT_ENCHANTMENT);

                if (!isDemoItem(luck)) {
                    messageList.add(EnchanterItemMessage.LUCK_APPLIED);
                }

                messageList.add(EnchanterItemMessage.SUCCESS_RATE);
                messageList.add(EnchanterItemMessage.DESTROY_RATE);

                if (isDemoItem(protection)) {
                    messageList.add(EnchanterItemMessage.NOT_PROTECTED);
                } else {
                    messageList.add(EnchanterItemMessage.PROTECTED);
                }
            }

        }

        return toArray(messageList);
    }

    private boolean hasErrors(Inventory inv) {
        for (EnchanterItemMessage msg : getMessages(inv)) {
            if (msg == EnchanterItemMessage.NO_ITEM || msg == EnchanterItemMessage.NO_RUNE
                    || msg == EnchanterItemMessage.UNCHANGED || msg == EnchanterItemMessage.NOT_APPLICABLE
                    || msg == EnchanterItemMessage.LOW_LEVEL || msg == EnchanterItemMessage.MAX_LEVEL) return true;
        }
        return false;
    }

    private EnchanterItemMessage[] toArray(List<EnchanterItemMessage> list) {
        EnchanterItemMessage[] array = new EnchanterItemMessage[list.size()];
        int index = 0;
        for (EnchanterItemMessage msg : list) {
            array[index++] = msg;
        }
        return array;
    }

    private boolean chance(int successRate) {
        int chance = new Random().nextInt(100) + 1;
        System.out.println(successRate + " : " + chance);
        if (chance <= successRate) return true;
        return false;
    }


    private void update(Inventory inv, int resultSlot) {

        if (hasErrors(inv)) {
            inv.setItem(resultSlot, new ItemStack(Material.REDSTONE_BLOCK));
            new EnchanterResultant(inv.getItem(resultSlot)).setMessages(getMessages(inv));
            return;
        }

        inv.setItem(resultSlot, new ItemStack(Material.EMERALD_BLOCK));
        Rune rune = new Rune(inv.getItem(21));
        ApplicableItem item = new ApplicableItem(inv.getItem(19));
        int netLevel = rune.getLevel();
        int additionalLevel = rune.getLevel();
        int existingLevel = item.getLevel(rune.getEnchantment());

        if (additionalLevel == existingLevel) {
            netLevel = existingLevel + 1;
        } else {
            if (additionalLevel > existingLevel) {
                netLevel = additionalLevel;
            }
        }
        LuckStone ls = null;

        if (LuckStone.isLuckStone(inv.getItem(25))) {
            ls = new LuckStone(inv.getItem(25));
        }

//                                    new EnchanterResultant(inv.getItem(resultSlot)).setMessages(getMessages(inv));
        new EnchanterResultant(inv.getItem(resultSlot)).setMessages(rune, netLevel, ls, getMessages(inv));
    }
}

enum GlassColor {
    RED, GREEN;
}