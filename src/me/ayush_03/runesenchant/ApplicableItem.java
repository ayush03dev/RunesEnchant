package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicableItem {

    private ItemStack item;
    private int slots;
    private int lineIndex;
    private boolean initialized;

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
                    break;
                }
            }

            if (!initialized) {
                this.lineIndex = lore.size();
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

        if (!initialized) {
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

    public Map<CustomEnchant, Integer> getAllCustomEnchantments() {
        Map<CustomEnchant, Integer> map = new HashMap<>();

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String str : item.getItemMeta().getLore()) {
                if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("ce-")) {
                    String hidden = HiddenStringUtils.extractHiddenString(str);
                    hidden = hidden.replace("ce-", "");
                    String[] args = hidden.split(":");
                    map.put(CustomEnchant.fromString(args[0]), Integer.parseInt(args[1]));
                }
            }
        }
        return map;
    }

    public Response addEnchantment(CustomEnchant ce, int level) {
        if (!hasEnchantment(ce)) {

            ItemMeta meta = item.getItemMeta();
//            if (meta == null) return false;

            if (meta.hasLore()) {
                List<String> lore = meta.getLore();

                if (initialized) {
                    if (Settings.getInstance().slotsEnabled()) {
                        if (slots == 0) return Response.NO_SLOT;
                        setSlots(slots - 1);

                        String slotDisplay = Settings.getInstance().getSlotsDisplay();
                        slotDisplay = slotDisplay.replace("%slots%", slots + "" + HiddenStringUtils.encodeString("slots:" + slots));
                        slotDisplay = ChatColor.translateAlternateColorCodes('&', slotDisplay);

                        lore.set(lineIndex, slotDisplay);
                    }

                } else {
                    // TODO: Add..
                    if (Settings.getInstance().slotsEnabled()) {
                        setSlots(slots - 1);
                        String slotDisplay = Settings.getInstance().getSlotsDisplay();
                        slotDisplay = slotDisplay.replace("%slots%", slots + "" + HiddenStringUtils.encodeString("slots:" + slots));
                        slotDisplay = ChatColor.translateAlternateColorCodes('&', slotDisplay);
                        lore.add(slotDisplay);
                    }
                }
                lore.add(ce.getDisplayName(level) + HiddenStringUtils.encodeString("ce-" + ce.toString() + ":" + level));
                meta.setLore(lore);
                item.setItemMeta(meta);
                return Response.SUCCESS;

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
        return Response.SUCCESS;
    }

    public void setLevel(CustomEnchant ce, int level) {
        if (level <= ce.getMaxLevel()) {
            if (hasEnchantment(ce)) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                lore.set(getEnchantmnentIndex(ce), ce.getDisplayName(level) + HiddenStringUtils.encodeString("ce-" + ce.toString() + ":" + level));
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
        }
    }

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

    private int getEnchantmnentIndex(CustomEnchant ce) {
        int index = -1;
        for (String str : item.getItemMeta().getLore()) {
            index++;
            if (HiddenStringUtils.hasHiddenString(str) && HiddenStringUtils.extractHiddenString(str).contains("ce-")) {
                String hidden = HiddenStringUtils.extractHiddenString(str);
                hidden = hidden.replace("ce-", "");
                String[] args = hidden.split(":");
                if (ce == CustomEnchant.fromString(args[0])) return index;
            }
        }
        return -1;
    }
}