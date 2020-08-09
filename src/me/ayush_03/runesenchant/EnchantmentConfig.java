package me.ayush_03.runesenchant;

import org.bukkit.configuration.file.FileConfiguration;

public class EnchantmentConfig {

    private FileConfiguration fc;

    public EnchantmentConfig(CustomEnchant ce) {
        this.fc = FileManager.getInstance().getEnchantmentConfig(ce);
    }

    public FileConfiguration getConfig() {
        return fc;
    }

    public boolean isEnabled() {
        return getConfig().getBoolean("enabled");
    }

    public int getMaxLevel() {
        return getConfig().getInt("max-level");
    }

    public String getDisplayName() {
        return getConfig().getString("display-name");
    }

}
