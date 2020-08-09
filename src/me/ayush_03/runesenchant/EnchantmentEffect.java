package me.ayush_03.runesenchant;

import me.ayush_03.runesenchant.utils.ExpressionResolver;
import org.bukkit.configuration.ConfigurationSection;

import javax.script.ScriptException;

public abstract class EnchantmentEffect {

    public int getChance(CustomEnchant ce, int level) {
        EnchantmentConfig config = ce.getConfig();
        ConfigurationSection section = config.getConfig().getConfigurationSection("chance");

        if (section == null) return 0;

        String expression = config.getConfig().getString(section + ".expression");
        int override = config.getConfig().getInt(section + ".override.level_" + level);

        if (override == 0) {
            try {
                return ExpressionResolver.getInstance().solve(expression);
            } catch (ScriptException e) {
                System.out.println("Invalid chance expression!");
                return 0;
            }
        } else {
            return override;
        }
    }
}
