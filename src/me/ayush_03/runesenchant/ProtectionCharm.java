package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

// "pc-PROTECTION:" + level + ":" + left

public class ProtectionCharm {

    int level, left, index;
    ItemStack item;
    ProtectionCharmConfig config;

    public ProtectionCharm(int level) {
        this.level = level;
        this.config = new ProtectionCharmConfig(level);
        this.left = config.getStartingDurability(level);
    }

    public ProtectionCharm(ItemStack item) {
        this.item = item;
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            int counter = -1;
            for (String str : item.getItemMeta().getLore()) {
                counter++;
                if (HiddenStringUtils.hasHiddenString(str) &&
                HiddenStringUtils.extractHiddenString(str).contains("pc-PROTECTION:")) {
                    String hidden = HiddenStringUtils.extractHiddenString(str);
                    hidden = hidden.replace("pc-", "");
                    String[] args = hidden.split(":");

                    this.level = Integer.parseInt(args[0]);
                    this.left = Integer.parseInt(args[1]);
                    this.index = counter;
                    this.config = new ProtectionCharmConfig(level, left);
                    break;

                }
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLeft(int left) {
        List<String> lore = item.getItemMeta().getLore();
        ItemMeta meta = item.getItemMeta();
        if (left == 0) {
            // Remove the protection charm lore...
            lore.remove(index);

        } else {
            this.left = left;
            lore.set(index, config.getLoreDisplay() + HiddenStringUtils.encodeString("pc-PROTECTION:" + level + ":" + left));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public void setLevel(int level) {
        this.level = level;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(index, config.getLoreDisplay() + HiddenStringUtils.encodeString("pc-PROTECTION:" + level + ":" + left));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public int getLeft() {
        return left;
    }

    public void createItem() {

    }
}
