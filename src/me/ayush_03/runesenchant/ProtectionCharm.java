package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import me.ayush_03.runesenchant.utils.RuneUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

// "pc-PROTECTION:" + level + ":" + left

public class ProtectionCharm {

    private int level, left, index;
    private final ItemStack item;
    private ProtectionCharmConfig config;

    public ProtectionCharm(int level) {
        this.level = level;
        this.config = new ProtectionCharmConfig(level);
        this.left = config.getStartingDurability(level);
        this.item = createItem();
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

                    this.level = Integer.parseInt(args[1]);
                    this.left = Integer.parseInt(args[2]);
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

    public void addToLore(List<String> lore) {
        lore.add(config.getLoreDisplay() + HiddenStringUtils.encodeString("pc-PROTECTION:" + level + ":" + left));
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

    public ItemStack createItem() {
        ItemStack item = RuneUtils.getInstance().buildItemStack(config.getItemId());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(config.getItemDisplayName() + HiddenStringUtils.encodeString("pc-PROTECTION:" + level + ":" + left));
        meta.setLore(config.getItemLore());
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isProtectionCharmItem(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().hasDisplayName()) {
            String displayName = item.getItemMeta().getDisplayName();
            if (HiddenStringUtils.hasHiddenString(displayName)) {
                if (HiddenStringUtils.extractHiddenString(displayName).contains("pc-PROTECTION")) return true;
            }
        }
        return false;
    }

    public static String[] getProtectionCharmData(ItemStack protectionCharmItem) {
        if (isProtectionCharmItem(protectionCharmItem)) {
            return HiddenStringUtils.extractHiddenString(protectionCharmItem.getItemMeta().getDisplayName()).split(":");
        }
        return null;
    }
}
