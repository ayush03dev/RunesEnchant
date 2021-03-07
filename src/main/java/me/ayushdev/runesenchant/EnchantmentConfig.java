package me.ayushdev.runesenchant;

import me.ayushdev.runesenchant.utils.ExpressionResolver;
import org.bukkit.configuration.file.FileConfiguration;

import javax.script.ScriptException;

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

    public int getCost(int level) {
        if (getConfigFile().getConfigurationSection("cost") == null) return 0;

        if (getConfigFile().getInt("cost.level_" + level) != 0) {
            return getConfigFile().getInt("cost.level_" + level);
        } else {
            if (getConfigFile().getString("cost.expression") != null) {
                String expression = getConfigFile().getString("cost.expression");
                    return ExpressionResolver.getInstance().solve(expression.replace("%level%", level + ""));
            }
        }
        return 0;
    }
}
