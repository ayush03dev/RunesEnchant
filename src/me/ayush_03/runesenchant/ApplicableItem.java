package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ApplicableItem {

    private ItemStack item;
    private int slots;
    private int lineIndex;
    private boolean initialized;
    private Action action;

    public ApplicableItem(ItemStack item) {
        this.item = item;

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            this.slots = getAvailableSlots();

            List<String> lore = item.getItemMeta().getLore();

            for (int i = 0; i < lore.size(); i++) {

                String str = lore.get(i);

                if (Settings.getInstance().slotsEnabled() && HiddenStringUtils.hasHiddenString(str) &&
                        HiddenStringUtils.extractHiddenString(str).contains("slots:")) {
                    this.lineIndex = i;
                    this.initialized = true;
                    this.action = Action.MODIFY;
                    break;
                }
            }

            if (!initialized) {
                this.lineIndex = lore.size();
                action = Action.ADD;
            }
        } else {
            this.initialized = false;
            this.slots = Settings.getInstance().slotsEnabled() ? Settings.getInstance().getSlots() : 1;
        }
    }

    public ItemStack getItemStack() {
        return item;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;

        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if (meta.hasLore()) {
            lore = meta.getLore();
        } else {
            lore = new ArrayList<>();
        }

        String line = Settings.getInstance().getSlotsDisplay().replace("%slots%", slots + "");
        line = ChatColor.translateAlternateColorCodes('&', line);

        if (action == Action.ADD) {
            lore.add(line);
        } else {
            lore.set(lineIndex, line);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public boolean hasEnchantment(CustomEnchant ce) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String str : item.getItemMeta().getLore()) {
                if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("ce-")) {
                    String hidden = HiddenStringUtils.extractHiddenString(str);
                    hidden = hidden.replace("ce-", "");
                    String[] args = hidden.split(":");
                    return ce == CustomEnchant.fromString(args[0]);
                }
            }
        }
        return false;
    }

    public boolean addEnchantment(CustomEnchant ce, int level) {
        if (!hasEnchantment(ce)) {

            ItemMeta meta = item.getItemMeta();
//            if (meta == null) return false;

            if (meta.hasLore()) {
                List<String> lore = meta.getLore();

                if (initialized) {
                    if (Settings.getInstance().slotsEnabled()) {
                        if (slots == 0) return false;
                        setSlots(slots-1);

                        String slotDisplay = Settings.getInstance().getSlotsDisplay();
                        slotDisplay = slotDisplay.replace("%slots%", slots + "" + HiddenStringUtils.encodeString("slots:" + slots));
                        slotDisplay = ChatColor.translateAlternateColorCodes('&', slotDisplay);

                        lore.set(lineIndex, slotDisplay);
                    }

                } else {
                    // TODO: Add..
                    if (Settings.getInstance().slotsEnabled()) {
                        setSlots(slots-1);
                        String slotDisplay = Settings.getInstance().getSlotsDisplay();
                        slotDisplay = slotDisplay.replace("%slots%", slots + "" + HiddenStringUtils.encodeString("slots:" + slots));
                        slotDisplay = ChatColor.translateAlternateColorCodes('&', slotDisplay);
                        lore.add(slotDisplay);
                    }
                }
                lore.add(ce.getDisplayName(level) + HiddenStringUtils.encodeString("ce-" + ce.toString() + ":" + level));
                meta.setLore(lore);
                item.setItemMeta(meta);
                return true;

            } else {
                List<String> list = new ArrayList<>();
                if (Settings.getInstance().slotsEnabled()) {
                    String slotDisplay = Settings.getInstance().getSlotsDisplay();
                    slotDisplay = slotDisplay.replace("%slots%", slots + "" + HiddenStringUtils.encodeString("slots:" + slots));
                    slotDisplay = ChatColor.translateAlternateColorCodes('&', slotDisplay);

                    list.add(slotDisplay);
                    list.add(ce.getDisplayName(level) + HiddenStringUtils.encodeString("ce-" + ce.toString() + ":" + level));
                    meta.setLore(list);
                }
            }

            item.setItemMeta(meta);
        }
        return true;
    }
//
//    public void setLevel(CustomEnchant ce, int level) {
//
//    }
//
    public int getLevel(CustomEnchant ce) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String str : item.getItemMeta().getLore()) {
                if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("ce-")) {
                    String hidden = HiddenStringUtils.extractHiddenString(str);
                    hidden = hidden.replace("ce-", "");
                    String[] args = hidden.split(":");
                    if (ce == CustomEnchant.fromString(args[0])) return Integer.parseInt(args[1]);
                }
            }
        }
        return 0;
    }
//
//    public boolean canUpgrade(CustomEnchant ce) {
//    }
//
//    }

    private int getAvailableSlots() {
        if (!Settings.getInstance().slotsEnabled()) return 1;

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {

            for (String str : item.getItemMeta().getLore()) {
                if (HiddenStringUtils.hasHiddenString(str) &&
                        HiddenStringUtils.extractHiddenString(str).contains("slots:")) {
                    String hidden = HiddenStringUtils.extractHiddenString(str);
                    hidden = hidden.replace("slots:", "");
                    return Integer.parseInt(hidden);
                }
            }

        }
        return Settings.getInstance().getSlots();
    }

}

enum Action {
    MODIFY, ADD;
}