package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ApplicableItem {

    ItemStack item;
    int slots;
    int lineInex;

    public ApplicableItem(ItemStack item) {
        this.item = item;

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            this.slots = getAvailableSlots();

            List<String> lore = item.getItemMeta().getLore();

            if (Settings.getInstance().slotsEnabled() && slots == Settings.getInstance().getSlots()) {
                this.lineInex = lore.size();
                return;
            }

            for (int i = 0; i < lore.size(); i++) {

                String str = lore.get(i);

                if (HiddenStringUtils.hasHiddenString(str) &&
                        HiddenStringUtils.extractHiddenString(str).contains("slots:")) {
                    this.lineInex = i;
                    break;
                }
            }
        } else {
            this.slots = Settings.getInstance().slotsEnabled() ? Settings.getInstance().getSlots() : 1;
            this.lineInex = 0;
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
        List<String> lore = meta.getLore();
        List<String> newLore = new ArrayList<>();

        String line;

        for (int i = 0; i < lore.size(); i++) {
            if (i == lineNumber) {
                line = Settings.getInstance().getSlotsDisplay().replace("%slots%", slots + "");
                line = ChatColor.translateAlternateColorCodes('&', line);
            } else {
                line = lore.get(i);
            }

            newLore.add(line);
            meta.setLore(newLore);
            item.setItemMeta(meta);
        }
    }

    public boolean hasEnchantment(CustomEnchant ce) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String str : item.getItemMeta().getLore()) {

            }
        }
    }

    public void addEnchantment(CustomEnchant ce, int level) {
        if (!hasEnchantment(ce)) {
            this.slots--;
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {

            } else {
                List<String> list = new ArrayList<>();
                if (Settings.getInstance().slotsEnabled()) {
                    String slotDisplay = Settings.getInstance().getSlotsDisplay();
                    slotDisplay = slotDisplay.replace("%slots%", slots + "");
                    slotDisplay = ChatColor.translateAlternateColorCodes('&', slotDisplay);

                    list.add(slotDisplay);
                }
            }
        }
    }

    public void setLevel(CustomEnchant ce, int level) {

    }

    public int getLevel(CustomEnchant ce) {

    }

    public boolean canUpgrade(CustomEnchant ce) {

    }

    private int getAvailableSlots() {
        if (!Settings.getInstance().slotsEnabled()) return 1;

        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> lore =  item.getItemMeta().getLore();
            boolean contains = false;

            for (String str : lore) {
                if (HiddenStringUtils.hasHiddenString(str) &&
                        HiddenStringUtils.extractHiddenString(str).contains("slots:")) {
                    String hidden = HiddenStringUtils.extractHiddenString(str);
                    hidden = hidden.replace("slots:", "");
                    int slots = Integer.parseInt(hidden);
                    return slots;
                }
            }

        } else {
            return Settings.getInstance().getSlots();
        }

        return 0;
    }

}
