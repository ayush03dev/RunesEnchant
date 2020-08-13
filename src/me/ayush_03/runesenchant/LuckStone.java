package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import me.ayush_03.runesenchant.utils.RuneUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LuckStone {

    int level;
    LuckStoneConfig config;

    public LuckStone(int level) {
        this.level = level;
        this.config = new LuckStoneConfig(level);
    }

    public LuckStone(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                HiddenStringUtils.hasHiddenString(item.getItemMeta().getDisplayName())) {

            String hidden = HiddenStringUtils.extractHiddenString(item.getItemMeta().getDisplayName());
            if (hidden.contains("ls")) {
                String[] args = hidden.split(":");
                this.level = Integer.parseInt(args[1]);
                this.config = new LuckStoneConfig(level);
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public int getIncrease() {
        return config.getIncrease();
    }

    public ItemStack getItem() {
        ItemStack item = RuneUtils.getInstance().buildItemStack(config.getItemId());
        if (item == null) return null;

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(config.getDisplayName() + HiddenStringUtils.encodeString("ls-LUCKY:" + level));
        meta.setLore(config.getItemLore());
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isLuckStone(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                && HiddenStringUtils.hasHiddenString(item.getItemMeta().getDisplayName())) {
            if (HiddenStringUtils.extractHiddenString(item.getItemMeta().getDisplayName()).contains("ls-")) return true;
        }
        return false;
    }

}
