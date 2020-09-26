package me.ayushdev.runesenchant;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentOrbConfig {

    FileConfiguration fc;
    int slots;

    public EnchantmentOrbConfig(int slots) {
        this.fc = FileManager.getInstance().getEnchantmentOrbConfig();
        this.slots = slots;
    }

    public String getItemId() {
        return fc.getString("item.item-id");
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', fc.getString("item.display-name")
        .replace("%slots%", slots + ""));
    }

    public List<String> getItemLore() {
        List<String> lore = new ArrayList<>();
        for (String str : fc.getStringList("item.lore")) {
            str = str.replace("%slots%", slots + "");
            lore.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        return lore;
    }

}
