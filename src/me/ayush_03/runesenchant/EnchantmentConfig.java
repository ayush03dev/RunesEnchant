package me.ayush_03.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;

public class EnchantmentConfig {

    private final FileConfiguration fc;

    public EnchantmentConfig(CustomEnchant ce) {
        this.fc = FileManager.getInstance().getEnchantmentConfig(ce);
    }

    public FileConfiguration getConfigFile() {
        return fc;
    }

    public boolean isEnabled() {
        return getConfigFile().getBoolean("enabled");
    }

    public int getMaxLevel() {
        return getConfigFile().getInt("max-level");
    }

    public String getLoreDisplay() {
        return getConfigFile().getString("lore-display");
    }

    public String getDisplayName() {
        return getConfigFile().getString("display-name");
    }
}
