package me.ayushdev.runesenchant;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum Message {

    ENCHANTMENT_SUCCESSFUL, ENCHANTMENT_UNSUCCESSFUL, ITEM_DESTROYED, NO_XP,
    NO_SLOT, INVALID_AMOUNT, ITEM_SAVED, MAX_LEVEL, PROTECTION_CHARM_RECEIVED, LUCK_STONE_RECEIVED,
    ENCHANTMENT_ORB_RECEIVED, RESURRECTION_STONE_RECEIVED, RUNE_RECEIVED, ITEM_DROPPED;

    FileConfiguration fc;

    Message() {
        fc = FileManager.getInstance().getMessageConfig();
    }

    @Override
    public String toString() {
       String msg = fc.getString(super.toString().toLowerCase().replace("_", "-"));
       msg = ChatColor.translateAlternateColorCodes('&', msg);

       return msg;
    }
}
