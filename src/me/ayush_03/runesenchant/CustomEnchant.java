package me.ayush_03.runesenchant;

import org.bukkit.ChatColor;

public enum CustomEnchant {

    AEGIS(EnchantType.AXE);

    private EnchantType type;
    private EnchantmentConfig config;

    CustomEnchant(EnchantType type) {
        EnchantType t = EnchantType.fromString(getConfig().getConfig().getString("enchant-type"));
        this.type = t == null ? type : t;
        this.config = new EnchantmentConfig(this);
    }

    public EnchantType getType() {
        return type;
    }

    public int getMaxLevel() {
        return config.getMaxLevel();
    }

    public static CustomEnchant fromString(String str) {
        for (CustomEnchant ce : values()) {
            if (ce.toString().equalsIgnoreCase(str)) return ce;
        }
        return null;
    }

    public EnchantmentConfig getConfig() {
        return config;
    }

    public String getDisplayName(int level) {
        String displayName = config.getDisplayName();
        displayName = displayName.replace("%level%", level + "");
        displayName = ChatColor.translateAlternateColorCodes('&', displayName);
        return displayName;
    }
}
