package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.ExpressionResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class LuckStoneConfig {

    FileConfiguration fc;
    int level;

    public LuckStoneConfig(int level) {
        this.fc = FileManager.getInstance().getLuckStoneConfig();
        this.level = level;
    }

    public String getItemId() {
        return fc.getString("item.item-id");
    }

    public String getDisplayName() {
        return ChatColor.translateAlternateColorCodes('&', fc.getString("item.display-name"));
    }

    public List<String> getItemLore() {
        List<String> lore = new ArrayList<>();
        for (String str : fc.getStringList("item.lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        return lore;
    }

    public int getIncrease() {
        String expression = fc.getString("increase.expression");
        int override = fc.getInt("increase.level_" + level);

        if (override == 0) {
            if (expression == null) return 0;
            try {
                return ExpressionResolver.getInstance().solve(expression.replace("%level%", level + ""));
            } catch (ScriptException e) {
                e.printStackTrace();
                Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Invalid expression in Lucky Stone Config!");
                return 0;
            }
        } else {
            return override;
        }
    }
}
