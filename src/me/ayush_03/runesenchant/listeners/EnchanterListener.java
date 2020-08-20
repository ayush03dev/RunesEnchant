package me.ayush_03.runesenchant.listeners;

import me.ayush_03.runesenchant.*;
import me.ayush_03.runesenchant.gui.EnchanterGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                if (holder.getPlayer().getUniqueId().equals(p.getUniqueId())) {

                   if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                       e.setCancelled(true);
                       return;
                   }

                    // To be reset later...
                    int resultSlot = 40;
                    int slot = e.getRawSlot();
                    Inventory inv = e.getInventory();

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
                                    item.addEnchantment(ce, rune.getLevel());

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

                            if (!isDemoItem(current)) {
                                e.setCancelled(true);
                                return;
                            }

                            if (cursor.getAmount() != 1) {
                                e.setCancelled(true);
                                p.sendMessage(ChatColor.RED + "Amount of item cannot be more than 1!");
                                return;
                            }

                            if (slot == 19) {
                                if (!ApplicableItem.isSupportedItem(cursor)) {
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
                                update(inv, slot, InventoryAction.SWAP_WITH_CURSOR, resultSlot, e.getCursor(),
                                        current);
                                p.setItemOnCursor(new ItemStack(Material.AIR));
                            }

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

                            update(inv, slot, InventoryAction.PICKUP_ALL, resultSlot, e.getCursor(),
                                    current);
                            e.setCancelled(true);
                            e.setCurrentItem(demo);
                            p.setItemOnCursor(current);
                        }

                    } else {
                        ItemStack current = e.getCurrentItem();
                        if (current == null || current.getType() == Material.AIR) return;
                        if (!Rune.isRune(current) && !ResurrectionStone.isRessurectionStone(current)
                                && !LuckStone.isLuckStone(current) && !ApplicableItem.isSupportedItem(current)) {
//                                && !LuckStone.isLuckStone(current) && current.getType() != Material.DIAMOND_SWORD) {
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
                // 19 23 25 27
                ItemStack item = inv.getItem(19);
                ItemStack rune = inv.getItem(21);
                ItemStack rs = inv.getItem(23);
                ItemStack ls = inv.getItem(25);
                ItemStack result = inv.getItem(40);

                ItemStack[] arr = new ItemStack[] {item, rune, rs, ls, result};
                    for (ItemStack itemStack : arr) {
                        if (!isGlass(itemStack) && !isDemoItem(itemStack)
                                && itemStack.getType() != Material.EMERALD_BLOCK &&
                                itemStack.getType() != Material.REDSTONE_BLOCK &&
                                itemStack.getType() != Material.BARRIER) {
                            p.getWorld().dropItem(p.getLocation(), itemStack);
                        }
                    }
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (e.getInventory().getHolder() instanceof GUIHolder) {
                for (int i : e.getRawSlots()) {
                    if (i < 54) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    private boolean isDemoItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return true;
        return item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.ARROW_DAMAGE);
    }

    private boolean isGlass(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return true;
        return item.getType().toString().contains("GLASS_PANE");
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
        meta.setDisplayName(ChatColor.BLACK + "");
        item.setItemMeta(meta);
        return item;
    }

    private EnchanterItemMessage[] getMessages(ItemStack item,
                                               ItemStack rune, ItemStack protection, ItemStack
                                                       luck) {
        // 19, 21, 23, 25 --- 31
        List<EnchanterItemMessage> messageList = new ArrayList<>(4);

        if (isDemoItem(item) && isDemoItem(rune)) {
            messageList.add(EnchanterItemMessage.DEFAULT);
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

                if (isDemoItem(protection) && !new ApplicableItem(item).hasProtectionCharm()) {
                    messageList.add(EnchanterItemMessage.NOT_PROTECTED);
                } else {
                    messageList.add(EnchanterItemMessage.PROTECTED);
                }
            }

        }

        return toArray(messageList);
    }


    private boolean hasErrors(ItemStack item, ItemStack rune, ItemStack rs, ItemStack ls) {
        for (EnchanterItemMessage msg : getMessages(item, rune, rs, ls)) {
            if (msg == EnchanterItemMessage.NO_ITEM || msg == EnchanterItemMessage.NO_RUNE
                    || msg == EnchanterItemMessage.DEFAULT || msg == EnchanterItemMessage.NOT_APPLICABLE
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
        if (chance <= successRate) return true;
        return false;
    }

    private void update(Inventory inv, int slot, InventoryAction action, int resultSlot,
                        ItemStack cursor, ItemStack current) {

        // 19 21 23 25
        ItemStack r = null;
        ItemStack i = null;
        ItemStack protection = null;
        ItemStack luck = null;

        if (slot == 19) {
            if (action == InventoryAction.SWAP_WITH_CURSOR) {
                i = cursor;
            } else {
                if (isDemoItem(current)) {
                    i = new EnchanterGUI().getDemoItem();
                }
            }
            r = inv.getItem(21);
            protection = inv.getItem(23);
            luck = inv.getItem(25);

        } else if (slot == 21) {
            if (action == InventoryAction.SWAP_WITH_CURSOR) {
                r = cursor;
            } else {
                if (isDemoItem(current)) {
                    r = new EnchanterGUI().getDemoRune();
                }
            }
            i = inv.getItem(19);
            protection = inv.getItem(23);
            luck = inv.getItem(25);
        } else if (slot == 23) {
            if (action == InventoryAction.SWAP_WITH_CURSOR) {
                protection = cursor;
            } else {
                if (isDemoItem(current)) {
                    protection = new EnchanterGUI().getDemoResurrectionStone();
                }
            }
            i = inv.getItem(19);
            r = inv.getItem(21);
            luck = inv.getItem(25);

        } else if (slot == 25) {
            if (action == InventoryAction.SWAP_WITH_CURSOR) {
                luck = cursor;
            } else {
                if (isDemoItem(current)) {
                    luck = new EnchanterGUI().getDemoLuckStone();
                }
            }
            i = inv.getItem(19);
            r = inv.getItem(21);
            protection = inv.getItem(23);
        }

        if (hasErrors(i, r, protection, luck)) {
            ItemStack redstoneBlock = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta itemMeta = redstoneBlock.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "NOT READY");
            redstoneBlock.setItemMeta(itemMeta);
            inv.setItem(resultSlot, redstoneBlock);
            new EnchanterResultant(inv.getItem(resultSlot)).setErrorMessage(getMessages(i, r, protection, luck)[0]);
            return;
        }

        ItemStack emeraldBlock = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta itemMeta = emeraldBlock.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "ENCHANT");
        emeraldBlock.setItemMeta(itemMeta);
        emeraldBlock.setItemMeta(itemMeta);

        inv.setItem(resultSlot, emeraldBlock);
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

        if (LuckStone.isLuckStone(luck)) {
            ls = new LuckStone(luck);
        }

        if (hasErrors(item.getItemStack(), r, protection, luck)) {
            new EnchanterResultant(inv.getItem(resultSlot)).setErrorMessage(getMessages(i, r, protection, luck)[0]);
        } else {
            new EnchanterResultant(inv.getItem(resultSlot)).setReadyMessages(rune, ls, netLevel, getMessages(i, r, protection, luck));
        }
    }
}

enum GlassColor {
    RED, GREEN;
}