package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.ExpressionResolver;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.script.ScriptException;
import java.util.Random;

public class EnchantmentEffect {

    public int getChance(CustomEnchant ce, int level) {
        EnchantmentConfig config = ce.getConfig();
        ConfigurationSection section = config.getConfigFile().getConfigurationSection("chance");

        if (section == null) return 0;

        int override = config.getConfigFile().getInt(section + "level_" + level);

        if (override == 0) {
            String expression = config.getConfigFile().getString(section + ".expression");
            if (expression == null) return 0;
            try {
                return ExpressionResolver.getInstance().solve(expression);
            } catch (ScriptException e) {
                e.printStackTrace();
                System.out.println("Invalid chance expression!");
                return 0;
            }
        } else {
            return override;
        }
    }

    public int getValue(CustomEnchant ce, int level, String path) {
        FileConfiguration config = ce.getConfig().getConfigFile();
        ConfigurationSection section = config.getConfigurationSection(path);
        if (section == null) return 0;

        int value = config.getInt(path + ".level_" + level);
        if (value == 0) {

            String expression = config.getString(path + ".expression");
            if (expression == null) return 0;

            try {
                return ExpressionResolver.getInstance().solve(expression);
            } catch (ScriptException e) {
                e.printStackTrace();
                System.out.println("Invalid expression!");
                return 0;
            }
        } else {
            return value;
        }
    }

    public int generateRandom() {
        return new Random().nextInt(100)+1;
    }
}
