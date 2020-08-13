package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.HiddenStringUtils;
import me.ayush_03.runesenchant.utils.RuneUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ResurrectionStone {

    FileConfiguration fc;

    public ItemStack getItem() {
        this.fc = FileManager.getInstance().getResurrectionStoneConfig();
        if (fc == null) return null;
        ItemStack item = RuneUtils.getInstance().buildItemStack(fc.getString("item-id"));
        if (item == null) return null;
        String displayName = ChatColor.translateAlternateColorCodes('&', fc.getString("display-name"));

        List<String> lore = new ArrayList<>();
        for (String str : fc.getStringList("lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', str));
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName + HiddenStringUtils.encodeString("rs-RESURRECTION"));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isRessurectionStone(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()
                && HiddenStringUtils.hasHiddenString(item.getItemMeta().getDisplayName())) {
            if (HiddenStringUtils.extractHiddenString(item.getItemMeta().getDisplayName()).contains("rs-")) return true;
        }
        return false;
    }

}