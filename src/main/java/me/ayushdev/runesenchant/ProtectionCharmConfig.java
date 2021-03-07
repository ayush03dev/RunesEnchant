package me.ayushdev.runesenchant;

import me.ayushdev.runesenchant.utils.ExpressionResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class ProtectionCharmConfig {

    int level, left;
    FileConfiguration config;

    public ProtectionCharmConfig(int level, int left) {
        this.level = level;
        this.left = left;
        this.config = FileManager.getInstance().getProtectionCharmConfig();
    }

    public ProtectionCharmConfig(int level) {
        this.config = FileManager.getInstance().getProtectionCharmConfig();
        this.level = level;
        this.left = getStartingDurability(level);
    }

    public boolean isEnabled() {
        return config.getBoolean("enabled");
    }

    public int getMaxLevel() {
        return config.getInt("max-level");
    }

    public String getLoreDisplay() {
        String display = config.getString("in-lore-display");
        display = display.replace("%level%", level + "");
        display = display.replace("%left%", left + "");
        display = ChatColor.translateAlternateColorCodes('&', display);
        return display;
    }

    public String getItemDisplayName() {
        String display = config.getString("item.display-name");
        display = display.replace("%level%", level + "");
        display = display.replace("%left%", left + "");
        display = ChatColor.translateAlternateColorCodes('&', display);
        return display;

    }

    public List<String> getItemLore() {
        List<String> lore = new ArrayList<>();
        for (String str : config.getStringList("item.lore")) {
            str = str.replace("%level%", level + "");
            str = str.replace("%left%", left + "");
            str = ChatColor.translateAlternateColorCodes('&', str);
            lore.add(str);
        }
        return lore;
    }

    public int getStartingDurability(int level) {
        String expression = config.getString("durability.expression");
        if (expression != null) {
            expression = expression.replace("%level%", level + "");
        }
        int override = config.getInt("durability.level_" + level);

        if (override == 0) {
            if (expression == null) return 0;
            if (expression.equalsIgnoreCase("unlimited")) {
                return -1;
            } else {
                return ExpressionResolver.getInstance().solve(expression);
            }
        } else {
            return override;
        }
    }

    public boolean isGlowing() {
        return config.getBoolean("item.glow");
    }

    public String getItemId() {
        return config.getString("item.item-id");
    }
}
